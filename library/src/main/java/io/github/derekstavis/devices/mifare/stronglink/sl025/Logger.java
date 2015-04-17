package io.github.derekstavis.devices.mifare.stronglink.sl025;

import java.util.logging.Level;

class Logger {
	
	public static void d(String message, Object... args) {
		doLog(Level.INFO, message, args);
	}
	
	public static void e(String message, Object... args) {
		doLog(Level.WARNING, message, args);
	}
	
	private static void doLog(Level level, String message, Object... args) {

		if (!Constants.DEBUG) return;

		String[] split = message.split(":", 2);
		String lLevel = level == Level.INFO ? "DEBUG" : "ERROR";
		
		if (split.length > 1) {
			System.out.println(String.format("[%s - %s]\t%s", lLevel, split[0], String.format(split[1], args)));
		} else {
			System.out.println(String.format("[%s - Unknown]\t%s", lLevel, String.format(message, args)));
		}
	}
	
	public static void debugArray(byte[] bytes) {

		if (!Constants.DEBUG) return;

		StringBuilder sb = new StringBuilder("DebugArray: Array -> [");
		for (byte b : bytes) {
			sb.append(String.format("%02X ", b));
		}
		sb.append("]");
		
		Logger.d(sb.toString());
	}
	
}
