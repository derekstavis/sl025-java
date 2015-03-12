package io.github.derekstavis.devices.mifare.stronglink.sl025;

public class InvalidPacketException extends Exception {
	
	public InvalidPacketException() {
		super("This packet was marked as invalid");
	}
	
}
