package eu.teamon.volley;

public class Command {
	public static String newPlayerRegistered(Player player){
		return "r " + player.getNick();
	}
	
	public static String chatMessage(Message message){
		return "c " + message.getAuthor().getNick() + " " + message.getContent();
	}
	
	public static String chatMessage(Player player, String message){
		return "c " + player.getNick() + " " + message;
	}
	
	public static String chatMessage(String message){
		return "c " + message;
	}
	
	public static String moveX(int x){
		return "m " + x;
	}
	
	public static String playerPosition(Player player){
		return "s " + player.getNick() + " " + player.getX() + " " + player.getY();
	}
}
