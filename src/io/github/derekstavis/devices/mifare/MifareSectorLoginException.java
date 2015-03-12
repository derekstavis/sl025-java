package io.github.derekstavis.devices.mifare;

public class MifareSectorLoginException extends Exception {
	
	public MifareSectorLoginException() {
		super("You must login to this sector before reading or writing it.");
	}
	
}
