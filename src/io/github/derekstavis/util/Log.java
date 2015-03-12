package io.github.derekstavis.util;

import io.github.derekstavis.Constants;

import java.util.logging.Level;

public class Log {
	
	public static void d(String message, Object... args) {
		doLog(Level.INFO, message, args);
	}
	
	public static void e(String message, Object... args) {
		doLog(Level.WARNING, message, args);
	}
	
	private static void doLog(Level level, String message, Object... args) {
		String[] split = message.split(":", 2);

		if (!Constants.DEBUG) return;

		String lLevel = level == Level.INFO ? "DEBUG" : "ERROR";
		
		if (split.length > 1) {
			System.out.println(String.format("[%s - %s]\t%s", lLevel, split[0], String.format(split[1], args)));
		} else {
			System.out.println(String.format("[%s - Unknown]\t%s", lLevel, String.format(message, args)));
		}
	}
	
	public static void debugArray(byte[] bytes) {
		StringBuilder sb = new StringBuilder("DebugArray: Array -> [");
		for (byte b : bytes) {
			sb.append(String.format("%02X ", b));
		}
		sb.append("]");
		
		Log.d(sb.toString());
	}
	
}
