package io.github.derekstavis.devices.mifare;

public class MifareUnknownBlockException extends Exception {

	public MifareUnknownBlockException() {
		super("Tried to read or write an unknown block");
	}
	
}
