package io.github.derekstavis.devices.mifare.stronglink.sl025;

public enum Status {
	SUCCESS 				((byte) 0x00),
	NO_TAG 					((byte) 0x01),
	SUCCESS_LOGIN			((byte) 0x02),
	FAIL_LOGIN				((byte) 0x03),
	FAIL_READ 				((byte) 0x04),
	FAIL_WRITE 				((byte) 0x05),
	FAIL_READ_AFTER_WRITE 	((byte) 0x06),
	ADDRESS_OVERFLOW 		((byte) 0x08),
	FAIL_DOWNLOAD_KEY 		((byte) 0x09),
	NOT_AUTHENTICATED		((byte) 0x0D),
	NOT_VALUE_BLOCK 		((byte) 0x0E),
	FAIL_CHECKSUM			((byte) 0xF0),
	UNKNOWN_COMMAND 		((byte) 0xF1),
	UNKNOWN_STATUS			((byte) 0xFF);

	private final byte value;
	Status(byte value) {
		this.value = value;
	}

	public byte getByte() {
		return value;
	}

	public static Status fromByte(byte b) {
		for (Status c : Status.values()) {
			if (c.getByte() == b) {
				return c;
			}
		}
		
		return UNKNOWN_STATUS;
	}
}
