package eu.teamon.volley;

public class Game {
    public static final float TIME = 0.15f;
    public static final float GRAVITY = 0.003f; // 9.81
    
    private static final int WAITING 	= 1;
    private static final int RUNNING	= 2;
    private static final int SCORED		= 3;
    
	private Server server;
	private SmartThread gameThread;
	private Ball ball;
	private int state;
	
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
				state = SCORED;
				
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
	
	public boolean isWaiting(){ return state == WAITING; }
	
	public void serve(Player player){
		if(player == findPlayerWithBall()){
			state = RUNNING;
		}
	}
	
	public void process(){
		if(state == SCORED){
			Player player = findPlayerWithBall();
			if(player != null){
				ball.setPosition(new Vec(player.getSide()*0.5f, 1f));
			}
			
			state = WAITING;
		}
		
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
			
			if(player.isJumping() || player.pos.y > 0){
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
				
				if(player.getSide()*pos.x > Player.X_MAX) pos.x = player.getSide()*Player.X_MAX;
				else if(player.getSide()*pos.x < 0) pos.x = 0f;
				moved = true;
			}
			
			// check contact with ball
			
			playerTouchingBall(player);
			
			

			if(moved){
				server.sendToAll(Command.playerPosition(player));
			}
		}
		
		if(state == RUNNING){
			Vec pos = ball.getPosition();
			
			if(pos.y >= 0){
				Vec vel = ball.getVelocity();
				
				vel.y -= GRAVITY*(TIME/2);
				
				pos.x += vel.x*(TIME/2);
				pos.y += vel.y*(TIME/2);
				if(pos.y < Ball.SIZE/2) pos.y = Ball.SIZE/2;
				server.sendToAll(Command.ballPosition(ball));
			}
		}
	}
	
	public void playerTouchingBall(Player player){
		Vec playerPos = player.getPosition().add(new Vec(0, Player.HEIGHT));
		
		Vec nbp = ball.getPosition().subtract(playerPos);
		Vec pp = new Vec(0,0);
		
		Logger.debug("ball: " + ball.getPosition() + ", player: " + player.getPosition() + "   =>   nbp = " + nbp);
		
		if(nbp.y >= 0){ // contact top
			//Logger.debug(ball.getPosition().distanceTo(player.getPosition()) + "     dist = " + nbp.distanceTo(pp) + ", <= " + (Player.WIDTH/2 + Ball.SIZE/2));
			if(nbp.distanceTo(pp) <= (Player.WIDTH/2 + Ball.SIZE/2)){
				Logger.debug("Contact TOP!");
//				System.exit(0);
				Vec nbv = ball.getVelocity().subtract(player.getVelocity());
				float a = (-nbv.getAngle()) + 2*((float)(Math.PI/2) - nbp.getAngle());
				Logger.debug(nbv + " ->" + nbv.withAngle(a));
				ball.setVelocity(nbv.withAngle(a).add(player.getVelocity()));				
			}
		} else {
			if(nbp.x >= -(Player.WIDTH/2 + Ball.SIZE/2) && nbp.x <= (Player.WIDTH/2 + Ball.SIZE/2)){ // -A <= x <= A
				Logger.debug("Contact SIDE!");
				// Vbx = -(Vbx - Vpx) + Vpx = -Vbx + 2*Vpx
				// Vby = (Vby - Vpy) + Vpy = Vby
				ball.setVelocity(ball.getVelocity().subtract(player.getVelocity()).negateX().add(player.getVelocity()));
			}
		}
		
	}
}
