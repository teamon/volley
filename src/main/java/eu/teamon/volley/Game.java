package eu.teamon.volley;

public class Game {
	private Server server;
	private SmartThread gameThread;
	
	public Game(Server server){
		this.server = server;
	}
	
	public void start(){
		gameThread = new SmartThread(){
			public void run(){
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
			if(player.isMoving()){
				player.move();
				server.sendToAll(Command.playerPosition(player));
			}
		}
	}
}
