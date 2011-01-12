package eu.teamon.volley.common;

/**
 * Simple class for easier logging
 * 
 * Set logger level to display only interesting informations
 */
public class Logger {
    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;
    
    
    private static int level = INFO;
    
    /**
     * Set logger level
     * @param i level
     */
    public static void setLevel(int i){
        level = i;
    }
	    
	/**
	 * Log DEBUG message
	 * @param msg
	 */
    public static void debug(String msg){
        if(level <= DEBUG) log("DEBUG", msg);
    } 
    
    /**
     * Log INFO message
     * @param msg
     */
    public static void info(String msg){
        if(level <= INFO) log("INFO", msg);
    }
    
    /**
     * Log WARN message
     * @param msg
     */
    public static void warn(String msg){
        if(level <= WARN) log("WARN", msg);
    }
    
    /**
     * Log ERROR message
     * @param msg
     */
    public static void error(String msg){
        if(level <= ERROR) log("ERROR", msg);
    }
    
    /**
     * Print formatted message to STDOUT
     * @param type
     * @param msg
     */
    private static void log(String type, String msg){
        System.out.println("[" + type + "] " + msg);
    }
}
