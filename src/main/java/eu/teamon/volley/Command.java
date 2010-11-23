package eu.teamon.volley;

public class Command {
	public static String newPlayerRegistered(Player player){
		return "r " + player.getNick();
	}
	
	public static String chatMessage(Player player, String message){
		return "c " + player.getNick() + " " + message;
	}
	
	public static String chatMessage(String message){
		return "c " + message;
	}
	
	public static String movingLeft(boolean moving){
		return "m l " + (moving ? 1 : 0);
	}
	
	public static String movingRight(boolean moving){
		return "m r " + (moving ? 1 : 0);
	}
	
	public static String playerPosition(Player player){
		return "s " + player.getNick() + " " + player.getX() + " " + player.getY();
	}
}
