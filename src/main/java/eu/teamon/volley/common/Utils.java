package eu.teamon.volley.common;

import java.util.Arrays;

/**
 * Collection of various utilities
 */
public class Utils {
	/**
	 * Join array into string using separator
	 * 
	 * For example: Utils.join(new String[]{"a", "b", "c"}, "+") => "a+b+c"
	 * 
	 * @param arr array of Objects
	 * @param separator
	 * @see #join(int[], String)
	 * @see #join(int[], String, String, String)
	 * @see #join(Object[], String, String, String)
	 */
	public static String join(Object[] arr, String separator){
		return join(arr, "", separator, "");
	}
	
	/**
	 * Join array into string using separator and some string at the beginning and end
	 * 
	 * For example: Utils.join(new String[]{"a", "b", "c"}, "<", "+", ">") => "<a+b+c>"
	 * 
	 * @param arr array of Objects
	 * @param begin String for the beginning
	 * @param separator
	 * @param end String for end
	 * @see #join(int[], String)
	 * @see #join(int[], String, String, String)
	 * @see #join(Object[], String)
	 */
	public static String join(Object[] arr, String begin, String separator, String end){
		return formatJoin(Arrays.toString(arr), begin, separator, end);
	}
	
	/**
	 * Join int[] array into string using separator
	 * 
	 * For example: Utils.join(new int[]{1,2,3}, "+") => "1+2+3"
	 * 
	 * @param arr array of ints
	 * @param separator
	 * @see #join(int[], String, String, String)
	 * @see #join(Object[], String)
	 * @see #join(Object[], String, String, String)
	 */
	public static String join(int[] arr, String separator){
		return join(arr, "", separator, "");
	}
	
	/**
	 * Join int[] array into string using separator
	 * 
	 * For example: Utils.join(new int[]{1,2,3}, "<", "+", ">") => "<1+2+3>"
	 * 
	 * @param arr array of Objects
	 * @param begin String for the beginning
	 * @param separator
	 * @param end String for end
	 * @see #join(int[], String)
	 * @see #join(Object[], String)
	 * @see #join(Object[], String, String, String)
	 */
	public static String join(int[] arr, String begin, String separator, String end){
		return formatJoin(Arrays.toString(arr), begin, separator, end);
	}
	
	private static String formatJoin(String res, String begin, String separator, String end){
		return res.replaceAll(", ", separator).replace("[", begin).replace("]", end);
	}

	/**
	 * Prepend Object into array, returning new array
	 * 
	 * For example: prepend("x", new String[]{"a", "b", "c"}) => new String[]{"x", "a", "b", "c"}
	 * @param item Object to be prepended
	 * @param arr Objects array
	 */
	public static Object[] prepend(Object item, Object[] arr) {
		Object[] result = new Object[arr.length+1];
		result[0] = item;
		System.arraycopy(arr, 0, result, 1, arr.length);
		return result;
	}
}
