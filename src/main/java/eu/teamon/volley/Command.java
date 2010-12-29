package eu.teamon.volley;

public class Command {
	public int id;
	public Object[] args;
	
	public Command(int id, Object... args){
		this.id = id;
		this.args = args;
	}
	
	public String toString(){
		return Utils.join(Utils.prepend(this.id, this.args), ":");
	}
	

	public static final int PLAYER_REGISTERED 	= 1;
	public static final int CHAT_MESSAGE 		= 2;
	public static final int MOVING_LEFT 		= 3;
	public static final int MOVING_RIGHT 		= 4;
	public static final int PLAYER_POSITION 	= 5;
	public static final int PLAYER_READY 		= 6;
	public static final int START_GAME			= 7;
	
	public static Command newPlayerRegistered(Player player){
		return new Command(PLAYER_REGISTERED, player.getNick());
	}
	
	public static Command chatMessage(Player player, String message){
		return new Command(CHAT_MESSAGE, player.getNick(), message);
	}
	
	public static Command chatMessage(String message){
		return new Command(CHAT_MESSAGE, message);
	}
	
//	public static Command moving(Player player){
//		return new Command(MOVING, player.is)
//	}
	

	public static Command movingLeft(boolean moving){
		return new Command(MOVING_LEFT, moving ? 1 : 0);
	}
	
	public static Command movingRight(boolean moving){
		return new Command(MOVING_RIGHT, moving ? 1 : 0);
	}
	
	public static Command playerPosition(Player player){
		return new Command(PLAYER_POSITION, player.getNick(), player.getX(), player.getY());
	}
	
	public static Command playerReady(){
		return new Command(PLAYER_READY);
	}
	
	public static Command startGame(){
		return new Command(START_GAME);
	}
}
