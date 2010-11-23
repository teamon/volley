package eu.teamon.volley;

public class Game {
	private Server server;
	private Thread gameThread;
	
	public Game(Server server){
		this.server = server;
	}
	
	public void start(){
		gameThread = new Thread(){
			public void run(){
				while(true) {
					process();
					try { Thread.sleep(10); } catch (InterruptedException e){ }
				}
			}
		};
		
		gameThread.start();
		Logger.debug("Game Thread started");
	}
	
	public void process(){
		for(Player player : server.getPlayers()){
			player.move();
			server.sendToAll(Command.playerPosition(player));
		}
	}
}
