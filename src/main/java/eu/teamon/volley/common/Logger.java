package eu.teamon.volley.common;

/**
 * Utils class for easier logging
 * 
 * @author teamon
 */
public class Logger {
    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;
    
    
    private static int level = DEBUG;
    
    public static void setLevel(int i){
        level = i;
    }
    
    public static void debug(String msg){
        if(level <= DEBUG) log("DEBUG", msg);
    } 
    
    public static void info(String msg){
        if(level <= INFO) log("INFO", msg);
    }
    
    public static void warn(String msg){
        if(level <= WARN) log("WARN", msg);
    }
    
    public static void error(String msg){
        if(level <= ERROR) log("ERROR", msg);
    }
    
    
    private static void log(String type, String msg){
        System.out.println("[" + type + "] " + msg);
    }
}
