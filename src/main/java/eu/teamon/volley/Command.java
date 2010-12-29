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
		String[] chunks = message.split(SEPARATOR, 2);
		return new Command(Integer.parseInt(chunks[0]), chunks[1].split(SEPARATOR));
	}
	
	public static final String SEPARATOR = "$";
	

	public static final int PLAYER_REGISTERED 	= 1;
	public static final int CLIENT_CHAT_MESSAGE = 2;
	public static final int SERVER_CHAT_MESSAGE = 2;
	public static final int MOVING_LEFT 		= 3;
	public static final int MOVING_RIGHT 		= 4;
	public static final int PLAYER_POSITION 	= 5;
	public static final int PLAYER_READY 		= 6;
	public static final int START_GAME			= 7;
	
	public static Command newPlayerRegistered(Player player){
		return new Command(PLAYER_REGISTERED, player.getNick());
	}
	
	public static Command chatMessage(Player player, String message){
		return new Command(SERVER_CHAT_MESSAGE, player.getNick(), message);
	}
	
	public static Command chatMessage(String message){
		return new Command(CLIENT_CHAT_MESSAGE, message);
	}
	
//	public static Command moving(Player player){
//		return new Command(MOVING, player.is)
//	}
	

	public static Command movingLeft(boolean moving){
		return new Command(MOVING_LEFT, moving ? "1" : "0");
	}
	
	public static Command movingRight(boolean moving){
		return new Command(MOVING_RIGHT, moving ? "1" : "0");
	}
	
	public static Command playerPosition(Player player){
		return new Command(PLAYER_POSITION, player.getNick(), Float.toString(player.getX()), Float.toString(player.getY()));
	}
	
	public static Command playerReady(){
		return new Command(PLAYER_READY);
	}
	
	public static Command startGame(){
		return new Command(START_GAME);
	}
}
