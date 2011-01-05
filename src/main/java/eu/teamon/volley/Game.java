package eu.teamon.volley;

public class Game {
	private Server server;
	private SmartThread gameThread;
	private Ball ball;
	
	public Game(Server server){
		this.server = server;
		this.ball = new Ball(new Vec<Float>(0f, .5f));
	}
	
	public Ball getBall(){
		return this.ball;
	}
	
	public void start(){
		gameThread = new SmartThread(){
			public void run(){
				for(Player player : server.getPlayers()){
					player.move();
					server.sendToAll(Command.playerPosition(player));
				}
				
				server.sendToAll(Command.ballPosition(ball));
				
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
	
	public void process(){
		for(Player player : server.getPlayers()){
			if(player.move()){
				server.sendToAll(Command.playerPosition(player));
			}
		}
	}
}
