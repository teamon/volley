package eu.teamon.volley;

public class Command {
	public int id;
	public String[] args;
	
	public Command(int id, String... args){
		this.id = id;
		this.args = args;
	}
	
	public String toString(){
		return Utils.join(Utils.prepend(this.id, this.args), SEPARATOR);
	}
	
	public static Command parse(String message){
		try {
			String[] chunks = message.split(SEPARATOR, 2);
			if(chunks.length < 2) return new Command(Integer.parseInt(chunks[0]));
			String[] args = chunks[1].split(SEPARATOR);
			return new Command(Integer.parseInt(chunks[0]), args);
		} catch (NumberFormatException e){
			Logger.error("Bad message: " + message);
		}
		return new Command(INVALID);
	}
	
	public static final String SEPARATOR = "#";
	

	public static final int INVALID				= -1;
	public static final int PLAYER_REGISTERED 	= 1;
	public static final int CLIENT_CHAT_MESSAGE = 2;
	public static final int SERVER_CHAT_MESSAGE = 3;
	public static final int MOVING_LEFT 		= 4;
	public static final int MOVING_RIGHT 		= 5;
	public static final int PLAYER_POSITION 	= 6;
	public static final int PLAYER_READY 		= 7;
	public static final int START_GAME			= 8;
	public static final int DISCONNECT			= 9;
	public static final int PLAYER_DISCONNECTED	= 10;
	public static final int STOP_GAME			= 11;
	public static final int BALL_POSITION		= 12;
	public static final int JUMPING				= 13;
	public static final int SERVE				= 14;
	public static final int SCORE				= 15;
	public static final int NEW_SET				= 16;
	
	public static Command newPlayerRegistered(Player player){
		return new Command(PLAYER_REGISTERED, player.getNick(), Integer.toString(player.getSide()));
	}
	
	public static Command chatMessage(Player player, String message){
		return new Command(SERVER_CHAT_MESSAGE, player.getNick(), message);
	}
	
	public static Command chatMessage(String message){
		return new Command(CLIENT_CHAT_MESSAGE, message);
	}	

	public static Command movingLeft(boolean moving){
		return new Command(MOVING_LEFT, moving ? "1" : "0");
	}
	
	public static Command movingRight(boolean moving){
		return new Command(MOVING_RIGHT, moving ? "1" : "0");
	}
	
	public static Command jumping(boolean jumping){
		return new Command(JUMPING, jumping ? "1" : "0");
	}
	
	public static Command playerPosition(Player player){
		Vec pos = player.getPosition();
		return new Command(PLAYER_POSITION, player.getNick(), Float.toString(pos.x), Float.toString(pos.y));
	}
	
	public static Command ballPosition(Ball ball){
		Vec pos = ball.getPosition();
		return new Command(BALL_POSITION, Float.toString(pos.x), Float.toString(pos.y));
	}
	
	public static Command score(Player player, int score){
		return new Command(SCORE, player.getNick(), Integer.toString(score));
	}
	
	public static Command newSet(int set){
		return new Command(NEW_SET, Integer.toString(set));
	}
	
	public static Command serve(){
		return new Command(SERVE);
	}
		
	public static Command playerReady(){
		return new Command(PLAYER_READY);
	}
	
	public static Command startGame(){
		return new Command(START_GAME);
	}
	
	public static Command disconnect(){
		return new Command(DISCONNECT);
	}
	
	public static Command playerDisconnected(Player player){
		return new Command(PLAYER_DISCONNECTED, player.getNick());
	}

}
