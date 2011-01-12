package eu.teamon.volley.common;

/**
 * Application configuration
 */
public class Config {
    public static final String 	DEFAULT_HOST 	= "localhost";
    public static final int 	DEFAULT_PORT 	= 7777;

    // player
    public static final float PLAYER_WIDTH 		= 10f;
    public static final float PLAYER_HEIGHT 	= 10f;
	public static final float PLAYER_MOVE_SPEED	= 1.2f;
    public static final float PLAYER_JUMP_SPEED = 1.2f;
    
    // ball
    public static final float BALL_RADIUS 		= 4f; 
    public static final float BALL_MAX_SPEED	= 5f;
    public static final float BALL_SLOWDOWN_FACTOR = 0.95f;
    
    // net
    public static final float NET_WIDTH		= 0.5f;
    public static final float NET_HEIGHT	= 25f;
    
    // world;
	public static final float TIME 		= 0.2f;
    public static final float GRAVITY 	= 1f;
    
    // rules
    public static final int SET_SCORE	= 3;
    public static final int SET_NUM		= 5;
}
