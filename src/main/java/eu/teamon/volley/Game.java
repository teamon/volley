package eu.teamon.volley;

public class Game {
    public static final float TIME = 0.4f;
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
		this.ball = new Ball(new FloatVec(0f, .5f));
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
					player.move();
					server.sendToAll(Command.playerPosition(player));
				}

		//		server.sendToAll(Command.ballPosition(ball));
				
				while(keep) {
					process();
					try { Thread.sleep(10); } catch (InterruptedException e){ }
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
				ball.setPos(player.getPos().add(new FloatVec(0f, 0.5f)));
			}
			
			state = WAITING;
		}
		
		server.sendToAll(Command.ballPosition(ball));

		for(Player player : server.getPlayers()){
			if(player.move()){
				server.sendToAll(Command.playerPosition(player));
			}
		}
		
		if(state == RUNNING) ball.move();
		server.sendToAll(Command.ballPosition(ball));
	}
}
