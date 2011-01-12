package eu.teamon.volley.server;

import eu.teamon.volley.common.Ball;
import eu.teamon.volley.common.Command;
import eu.teamon.volley.common.Config;
import eu.teamon.volley.common.Logger;
import eu.teamon.volley.common.SmartThread;
import eu.teamon.volley.common.Vec;

import static eu.teamon.volley.common.Config.*;

/**
 * Server game processing class
 * 
 * It does all computations
 */
public class Game {
	// game states
    private static final int WAITING 	= 1;
    private static final int RUNNING	= 2;
    
    /**
     * Server reference
     */
	private Server server;
	
	/**
	 * Computations thread
	 */
	private SmartThread gameThread;
	
	/**
	 * Ball representation
	 */
	private Ball ball;
	
	/**
	 * Current game state
	 */
	private int state;
	
	/**
	 * Current set number
	 */
	private int set = -1;
	
	/**
	 * Creates new game with server referenct
	 */
	public Game(Server server){
		this.server = server;
		this.ball = new Ball();
	}
	
	/**
	 * Returns current set number
	 */
	public int getSet(){
		return this.set;
	}
	
	/**
	 * Returns true if game is in WAITING state
	 */
	public boolean isWaiting(){ return state == WAITING; }
	
	/**
	 * Start game
	 * 
	 * Setup player and ball positions, notify clients
	 */
	public void start(){
		if(gameThread != null){
			stop();
		}
		
        // set players positions
        int i = 0;
        for(Player player : server.getPlayers()){
        	player.setSide(i*2 - 1);
        	server.sendToAll(Command.playerSide(player)); // just for color
        	i++;
        }
		
		gameThread = new SmartThread(){
			public void run(){
				newSet();
				setupAfterScore();
				
				for(Player player : server.getPlayers()){
					server.sendToAll(Command.playerPosition(player));
				}
				
				while(keep) {
					process();
					try { Thread.sleep(10); } catch (InterruptedException e){ }
				}
			}
		};
		
		gameThread.start();
		server.log("Server Game Thread started");
	}
	
	/**
	 * Stop game
	 */
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
	
	/**
	 * Game step - "the game's heart"
	 * 
	 * It does all the game computations
	 */
	public void process(){
		server.sendToAll(Command.ballPosition(ball));

		for(Player player : server.getPlayers()){
			boolean moved = false;
			
			Vec pos = player.getPosition();
			Vec vel = player.getVelocity();
			
			if(player.isMovingLeft()) {
				vel.x = -Config.PLAYER_MOVE_SPEED;
			} else if(player.isMovingRight()) {
				vel.x = Config.PLAYER_MOVE_SPEED;
			} else {
				vel.x = 0f;
			}
			
			if(state == RUNNING && (player.isJumping() || pos.y < 100)){
				vel.y += GRAVITY*TIME;
				pos.y += vel.y*TIME;
				
				pos.y = Math.min(Math.max(pos.y, 0), 100);
				
				if(pos.y == 100){ 
					player.setJumping(false);
					vel.y = 0f;
				}
				
				moved = true;
			}
			
			if(vel.x != 0){
				pos.x += vel.x*TIME;
				
				int side = player.getSide();
				
				if(side == 1){ // right
					pos.x = Math.min(Math.max(pos.x, 50 + NET_WIDTH/2 + PLAYER_WIDTH/2), 100 - PLAYER_WIDTH/2);
				} else { // left
					pos.x = Math.max(Math.min(pos.x, 50 - NET_WIDTH/2 - PLAYER_WIDTH/2), PLAYER_WIDTH/2);
						
				}

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
				ball.setVelocity(ball.getVelocity().negateX().scale(BALL_SLOWDOWN_FACTOR));
			}
			
			// ball touching ceiling
			if(pos.y <= BALL_RADIUS){
				ball.setVelocity(ball.getVelocity().negateY().scale(BALL_SLOWDOWN_FACTOR));
			}
			

			Vec nbp = pos.subtract(new Vec(50f, 100-NET_HEIGHT)).negate();
			
			// ball touching net side
			if(Math.abs(nbp.x) <= (BALL_RADIUS + NET_WIDTH/2) && nbp.y <= 0){
				ball.setVelocity(ball.getVelocity().negateX().scale(BALL_SLOWDOWN_FACTOR));
			}
			
			// ball touching net top
			if(nbp.y > 0 && nbp.getLength() <= (NET_WIDTH/2 + BALL_RADIUS)){
				if(ball.getVelocity().absoluteAngleTo(nbp) < Math.PI/2){
					float b = (float)Math.PI + nbp.getAngle() - ball.getVelocity().angleTo(nbp);
					ball.setVelocity(ball.getVelocity().withAngle(b));
				}
			}
			
			
			if(pos.y < (100-BALL_RADIUS)){
				Vec vel = ball.getVelocity();
				
				vel.y += 0.4f*GRAVITY*TIME;
				pos.x += vel.x*TIME;
				pos.y += vel.y*TIME;
				
				if(pos.y > (100 - BALL_RADIUS)) pos.y = 100 - BALL_RADIUS;
				server.sendToAll(Command.ballPosition(ball));
			} else {
				// score				
				for(Player player : server.getPlayers()){
					player.setHasBall(false);
				}
				
				Player player = findPlayerWithSide(pos.x > 50 ? -1 : 1);
				if(player != null){
					player.score(set);
					server.sendScore();
				
					if(player.getScore()[set] >= SET_SCORE){
						newSet();
					} else {
						player.setHasBall(true);
					}
				}
				
				setupAfterScore();
			}
		}
	}
	
	/**
	 * Find player that current hold ball
	 */
	private Player findPlayerWithBall(){
		for(Player player : server.getPlayers()){
			if(player.hasBall()) return player;
		}
		return null;
	}
	
	/**
	 * Find player with specified side parameter
	 */
	private Player findPlayerWithSide(int side){
		for(Player player : server.getPlayers()){
			if(player.getSide() == side) return player;
		}
		return null;
	}
	
	/**
	 * Make "serve" action
	 * 
	 * Checks if specified player holds the ball
	 */
	public void serve(Player player){
		if(player == findPlayerWithBall()){
			state = RUNNING;
		}
	}
	
	/**
	 * Start new game set or stop game if enough sets were played 
	 */
	private void newSet(){
		if(this.set < Config.SET_NUM-1){
			this.set++;
			
			server.log("New set: " + this.set);
			
			for(Player player : server.getPlayers()){
				player.setHasBall((player.getSide() == -1 && this.set % 2 == 0)	|| (player.getSide() == 1 && this.set % 2 == 1));
			}
			
			Player p = this.findPlayerWithBall();
			server.log("Player with ball: " + p.getNick());
			
			server.sendToAll(Command.newSet(this.set));
			server.sendScore();
		} else {
			stop();
		}
	}
	
	/**
	 * Setup players and ball position in start position after score
	 */
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
	
	/**
	 * Checks if player is touching a ball, if yes computes bounce
	 */
	private void playerTouchingBall(Player player){
		Vec playerPos = player.getPosition().subtract(new Vec(0, PLAYER_HEIGHT));
		Vec nbp = ball.getPosition().subtract(playerPos).negate();
		Vec pp = new Vec(0,0);
		
		if(nbp.y > 0 && nbp.distanceTo(pp) <= (PLAYER_WIDTH/2 + BALL_RADIUS)){
			Vec nbv = ball.getVelocity().subtract(player.getVelocity());		

			if(nbv.absoluteAngleTo(nbp) < Math.PI/2){
				float b = (float)Math.PI + nbp.getAngle() - nbv.angleTo(nbp);
				ball.setVelocity(nbv.withAngle(b).add(player.getVelocity()));
			}
		} else if(nbp.y < 0 && nbp.x >= -(PLAYER_WIDTH/2 + BALL_RADIUS) && nbp.x <= (PLAYER_WIDTH/2 + BALL_RADIUS)){ // -A <= x <= A
			ball.setVelocity(ball.getVelocity().subtract(player.getVelocity()).negateX().add(player.getVelocity()));
		}	
	}
}
