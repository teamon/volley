package eu.teamon.volley;

import java.util.Arrays;

public class Utils {
	public static String join(Object[] arr, String separator){
		if(arr.length == 0) return "";
		
		StringBuilder out = new StringBuilder();
		
		out.append(arr[0]);
		for(int i=1; i<arr.length; i++){
			out.append(separator);
			out.append(arr[i]);
		}
		
		return out.toString();
	}
	
	public static Object[] concat(Object[] first, Object[] second) {
		Object[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	public static Object[] append(Object[] arr, Object item) {
		Object[] result = Arrays.copyOf(arr, arr.length + 1);
		result[arr.length] = item;
		return result;
	}
	
	public static Object[] prepend(Object item, Object[] arr) {
		Object[] result = new Object[arr.length+1];
		result[0] = item;
		System.arraycopy(arr, 0, result, 1, arr.length);
		return result;
	}
}
