package eu.teamon.volley.common;

import java.util.Arrays;

public class Utils {
	public static String join(Object[] arr, String separator){
		return join(arr, "", separator, "");
	}
	
	public static String join(Object[] arr, String begin, String separator, String end){
		return formatJoin(Arrays.toString(arr), begin, separator, end);
	}
	
	public static String join(int[] arr, String separator){
		return join(arr, "", separator, "");
	}
	
	public static String join(int[] arr, String begin, String separator, String end){
		return formatJoin(Arrays.toString(arr), begin, separator, end);
	}
	
	private static String formatJoin(String res, String begin, String separator, String end){
		return res.replaceAll(", ", separator).replace("[", begin).replace("]", end);
	}

	public static Object[] prepend(Object item, Object[] arr) {
		Object[] result = new Object[arr.length+1];
		result[0] = item;
		System.arraycopy(arr, 0, result, 1, arr.length);
		return result;
	}
}
