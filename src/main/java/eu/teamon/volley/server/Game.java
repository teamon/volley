package eu.teamon.volley.server;

import eu.teamon.volley.common.Ball;
import eu.teamon.volley.common.Command;
import eu.teamon.volley.common.Config;
import eu.teamon.volley.common.Logger;
import eu.teamon.volley.common.SmartThread;
import eu.teamon.volley.common.Vec;

import static eu.teamon.volley.common.Config.*;

public class Game {
    private static final int WAITING 	= 1;
    private static final int RUNNING	= 2;
    private static final int SCORED		= 3;
    
	private Server server;
	private SmartThread gameThread;
	private Ball ball;
	private int state;
	private int set = -1;
	
	public Game(Server server){
		this.server = server;
		this.ball = new Ball();
	}
	
	public int getSet(){
		return this.set;
	}
	
	public boolean isWaiting(){ return state == WAITING; }
	
	public void start(){
		if(gameThread != null){
			stop();
		}
		
        // set players positions
        int i = 0;
        for(Player player : server.getPlayers()){
        	player.setSide(i*2 - 1);
        	server.sendToAll(Command.playerSide(player));
        	i++;
        }
		
		gameThread = new SmartThread(){
			public void run(){
				newSet();
				setupAfterScore();
				
				for(Player player : server.getPlayers()){
					server.sendToAll(Command.playerPosition(player));
				}

		//		server.sendToAll(Command.ballPosition(ball));
				
				while(keep) {
					process();
					try { Thread.sleep(5); } catch (InterruptedException e){ }
				}
			}
		};
		
		gameThread.start();
		Logger.debug("Server Game Thread started");
	}
	
	public void stop(){
		if(gameThread != null){
			gameThread.kill();
			server.sendToAll(Command.stopGame());
			for(Player player : server.getPlayers()){
				player.setReady(false);
			}
			
			this.set = 0;
		}
	}
	
	public void process(){
		server.sendToAll(Command.ballPosition(ball));

		for(Player player : server.getPlayers()){
			boolean moved = false;
			
			Vec pos = player.getPosition();
			Vec vel = player.getVelocity();
			
			if(player.isMovingLeft()) {
				vel.x = -Config.PLAYER_X_SPEED;
			} else if(player.isMovingRight()) {
				vel.x = Config.PLAYER_X_SPEED;
			} else {
				vel.x = 0f;
			}
			
			if(state == RUNNING && (player.isJumping() || pos.y < 100)){
				vel.y += GRAVITY*TIME;
				pos.y += vel.y*TIME;
				
				if(pos.y > 100) pos.y = 100f;
				else if(pos.y < 0) pos.y = 0;
				
				if(pos.y == 100){ 
					player.setJumping(false);
					vel.y = 0f;
				}
				
				moved = true;
			}
			
			if(vel.x != 0){
				pos.x += vel.x*TIME;
				
				int side = player.getSide();
				if(side*pos.x > (100 - PLAYER_WIDTH/2)) pos.x = side*(100 - PLAYER_WIDTH/2);
				else if(side*pos.x < (PLAYER_WIDTH/2 + NET_WIDTH/2)) pos.x = side*(PLAYER_WIDTH/2 + NET_WIDTH/2);
				moved = true;
			}
			
			// check contact with ball			
			if(state == RUNNING) playerTouchingBall(player);
			
			

			if(moved){
				server.sendToAll(Command.playerPosition(player));
			}
		}
		
		if(state == RUNNING){
			Vec pos = ball.getPosition();
			
			// ball touching side
			if(pos.x >= (100 - BALL_RADIUS) || pos.x <= BALL_RADIUS){
				ball.setVelocity(ball.getVelocity().negateX());
			}
			
			// ball touching ceiling
			if(pos.y <= BALL_RADIUS){
				ball.setVelocity(ball.getVelocity().negateY());
			}
			
			// ball touching net side
			if((50-pos.x) <= (BALL_RADIUS + NET_WIDTH/2) && pos.y <= (100-NET_HEIGHT)){
				ball.setVelocity(ball.getVelocity().negateX());
			}
			
			// ball touching net top
			if(pos.distanceTo(new Vec(0f, 100-NET_HEIGHT)) <= (NET_WIDTH/2 + BALL_RADIUS)){
				float a = (-ball.getVelocity().getAngle()) + (float)(Math.PI);
				ball.setVelocity(ball.getVelocity().withAngle(a));				
			}
			
			
			if(pos.y < (100-BALL_RADIUS)){
				Vec vel = ball.getVelocity();
				
				vel.y += GRAVITY*(TIME);
				pos.x += vel.x*(TIME);
				pos.y += vel.y*(TIME);
				
				if(pos.y > (100 - BALL_RADIUS)) pos.y = 100 - BALL_RADIUS;
				server.sendToAll(Command.ballPosition(ball));
			} else {
				Logger.debug("SCORE!!");
				// score!
				
				for(Player player : server.getPlayers()){
					player.setHasBall(false);
				}
				
				Player player = findPlayerWithSide(pos.x > 0 ? -1 : 1);
				player.score(set);
				server.sendScore();
				
				if(player.getScore()[set] >= SET_SCORE){
					newSet();
				} else {
					player.setHasBall(true);
				}
				
				setupAfterScore();
			}
		}
	}
	
	private Player findPlayerWithBall(){
		for(Player player : server.getPlayers()){
			if(player.hasBall()) return player;
		}
		return null;
	}
	
	private Player findPlayerWithSide(int side){
		for(Player player : server.getPlayers()){
			if(player.getSide() == side) return player;
		}
		return null;
	}
	
	public void serve(Player player){
		if(player == findPlayerWithBall()){
			state = RUNNING;
		}
	}
	
	private void newSet(){
		if(this.set < Config.SET_NUM-1){
			this.set++;
			
			Logger.debug("New set: " + this.set);
			
			for(Player player : server.getPlayers()){
				player.setHasBall((player.getSide() == -1 && this.set % 2 == 0)	|| (player.getSide() == 1 && this.set % 2 == 1));
			}
			
			Player p = this.findPlayerWithBall();
			Logger.debug("Player with ball: " + p.getNick());
			
			server.sendToAll(Command.newSet(this.set));
			server.sendScore();
		} else {
			stop();
		}
	}
	
	private void setupAfterScore(){
		for(Player player : server.getPlayers()){
			player.setStartPosition();
			server.sendToAll(Command.playerPosition(player));
		}
		
		Player player = findPlayerWithBall();
		if(player != null){
			ball.setPosition(new Vec(player.getSide()*25 + 50, 50));
			ball.setVelocity(new Vec(0f, 0f));
		}
		
		state = WAITING;
	}
	
	private void playerTouchingBall(Player player){
		Vec playerPos = player.getPosition().add(new Vec(0, PLAYER_HEIGHT));
		
		Vec nbp = ball.getPosition().subtract(playerPos);
		Vec pp = new Vec(0,0);
		
		if(nbp.y >= 0){ // contact top
			if(nbp.distanceTo(pp) <= (PLAYER_WIDTH/2 + BALL_RADIUS)){
				Vec nbv = ball.getVelocity().subtract(player.getVelocity());
				float a = (-nbv.getAngle()) + 2*((float)(Math.PI/2) - nbp.getAngle());
				ball.setVelocity(nbv.withAngle(a).add(player.getVelocity()));				
			}
		} else {
			if(nbp.x >= -(PLAYER_WIDTH/2 + BALL_RADIUS) && nbp.x <= (PLAYER_WIDTH/2 + BALL_RADIUS)){ // -A <= x <= A
				ball.setVelocity(ball.getVelocity().subtract(player.getVelocity()).negateX().add(player.getVelocity()));
			}
		}
		
	}
	

}
