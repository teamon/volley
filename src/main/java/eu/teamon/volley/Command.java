package eu.teamon.volley;

public class Command {
	public static String newPlayerRegistered(Player player){
		return "r " + player.getNick();
	}
	
	public static String chatMessage(Message message){
		return "c " + message.getAuthor().getNick() + " " + message.getContent();
	}
}
