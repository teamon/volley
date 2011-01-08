package eu.teamon.volley;

public class Game {
    // Game settings
	public static final float TIME = 0.15f;
    public static final float GRAVITY = 0.003f; // 9.81
    
    public static final float NET_WIDTH = 0.01f;
    public static final float NET_HEIGHT = 0.7f;
    
    
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
		this.ball = new Ball(new Vec(0f, 1f));
	}
	
	public Ball getBall(){
		return this.ball;
	}
	
	public void start(){
		if(gameThread != null){
			stop();
		}
		
		gameThread = new SmartThread(){
			public void run(){
				setupAfterScore();
				newSet();
				
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
		gameThread.kill();
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
	
	public boolean isWaiting(){ return state == WAITING; }
	
	public void serve(Player player){
		if(player == findPlayerWithBall()){
			state = RUNNING;
		}
	}
	
	public void newSet(){
		this.set++;
		server.sendToAll(Command.newSet(this.set));
		sendScore();		
	}
	
	public void sendScore(){
		for(Player player : server.getPlayers()){
			server.sendToAll(Command.score(player, player.getScore()[set]));
		}
	}
	
	public void process(){
		server.sendToAll(Command.ballPosition(ball));

		for(Player player : server.getPlayers()){
			boolean moved = false;
			
			Vec pos = player.getPosition();
			Vec vel = player.getVelocity();
			
			if(player.isMovingLeft()) {
				vel.x = -Player.X_SPEED;
			} else if(player.isMovingRight()) {
				vel.x = Player.X_SPEED;
			} else {
				vel.x = 0f;
			}
			
			if(state == RUNNING && (player.isJumping() || player.pos.y > 0)){
				vel.y -= GRAVITY*TIME;
				pos.y += vel.y*TIME;
				
				if(pos.y < 0) pos.y = 0f;
				else if(pos.y > Player.Y_MAX) pos.y = Player.Y_MAX;
				
				if(pos.y == 0){ 
					player.setJumping(false);
					vel.y = 0f;
				}
				
				moved = true;
			}
			
			if(vel.x != 0){
				pos.x += vel.x*TIME;
				
				int side = player.getSide();
				if(side*pos.x > (Player.X_MAX - Player.WIDTH/2)) pos.x = side*(Player.X_MAX - Player.WIDTH/2);
				else if(side*pos.x < (Player.WIDTH/2 + NET_WIDTH/2)) pos.x = side*(Player.WIDTH/2 + NET_WIDTH/2);
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
			if((pos.x + Ball.SIZE/2) >= 1.0 || (pos.x - Ball.SIZE/2) <= -1.0){
				ball.setVelocity(ball.getVelocity().negateX());
			}
			
			// ball touching ceiling
			if((pos.y + Ball.SIZE/2) >= 2.0){
				ball.setVelocity(ball.getVelocity().negateY());
			}
			
			// ball touching net side
			if(Math.abs(pos.x) <= (Ball.SIZE/2 + NET_WIDTH/2) && pos.y < NET_HEIGHT){
				ball.setVelocity(ball.getVelocity().negateX());
			}
			
			if(pos.y > Ball.SIZE/2){
				Vec vel = ball.getVelocity();
				
				vel.y -= GRAVITY*(TIME/2);
				
				pos.x += vel.x*(TIME/2);
				pos.y += vel.y*(TIME/2);
				if(pos.y < Ball.SIZE/2) pos.y = Ball.SIZE/2;
				server.sendToAll(Command.ballPosition(ball));
			} else {
				Logger.debug("SCORE!!");
				// score!
				
				for(Player player : server.getPlayers()){
					player.setHasBall(false);
				}
				
				Player player = findPlayerWithSide(pos.x > 0 ? -1 : 1);
				player.score(set);
				
				
				
				player.setHasBall(true);
				
				sendScore();				
				setupAfterScore();
			}
		}
	}
	
	public void setupAfterScore(){
		for(Player player : server.getPlayers()){
			player.setStartPosition();
			server.sendToAll(Command.playerPosition(player));
		}
		
		Player player = findPlayerWithBall();
		if(player != null){
			ball.setPosition(new Vec(player.getSide()*0.5f, 1f));
			ball.setVelocity(new Vec(0f, 0f));
		}
		
		state = WAITING;
	}
	
	public void playerTouchingBall(Player player){
		Vec playerPos = player.getPosition().add(new Vec(0, Player.HEIGHT));
		
		Vec nbp = ball.getPosition().subtract(playerPos);
		Vec pp = new Vec(0,0);
		
		if(nbp.y >= 0){ // contact top
			if(nbp.distanceTo(pp) <= (Player.WIDTH/2 + Ball.SIZE/2)){
				Vec nbv = ball.getVelocity().subtract(player.getVelocity());
				float a = (-nbv.getAngle()) + 2*((float)(Math.PI/2) - nbp.getAngle());
				ball.setVelocity(nbv.withAngle(a).add(player.getVelocity()));				
			}
		} else {
			if(nbp.x >= -(Player.WIDTH/2 + Ball.SIZE/2) && nbp.x <= (Player.WIDTH/2 + Ball.SIZE/2)){ // -A <= x <= A
				ball.setVelocity(ball.getVelocity().subtract(player.getVelocity()).negateX().add(player.getVelocity()));
			}
		}
		
	}
}
