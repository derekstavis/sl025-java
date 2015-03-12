package io.github.derekstavis.devices.mifare.stronglink.sl025;

public enum Command {
	SELECT_CARD 			((byte) 0x01),
	LOGIN_SECTOR 			((byte) 0x02),
	READ_BLOCK 				((byte) 0x03),
	WRITE_BLOCK 			((byte) 0x04),
	READ_VALUE_BLOCK 		((byte) 0x05),
	INIT_VALUE_BLOCK 		((byte) 0x06),
	WRITE_MASTER_KEY 		((byte) 0x07), 
	INCREMENT_VALUE 		((byte) 0x08),
	DECREMENT_VALUE 		((byte) 0x09),
	COPY_VALUE 				((byte) 0x0A),
	READ_DATA_PAGE 			((byte) 0x10), // (UltraLight & NTAG203) 
	WRITE_DATA_PAGE 		((byte) 0x11), // (UltraLight & NTAG203) 
	DOWNLOAD_KEY 			((byte) 0x12),
	LOGIN_VIA_STORED_KEY 	((byte) 0x13),
	MANAGE_LED 				((byte) 0x40),
	GET_FIRMWARE_VERSION 	((byte) 0xF0);
	
	private final byte command;
	Command(byte cmd) {
		command = cmd;
	}
	
	public final byte getByte() {
		return command;
	}

	public static Command fromByte(byte b) {
		for (Command c : Command.values()) {
			if (c.getByte() == b) {
				return c;
			}
		}
		
		return null;
	}
}
