package io.github.derekstavis.devices.mifare.stronglink.sl025;

import io.github.derekstavis.devices.mifare.MifareKey;

public enum MifareKeyType implements MifareKey.Type {
	KEY_A (new byte[] { (byte) 0xAA }), 
	KEY_B (new byte[] { (byte) 0xBB });
	
	private final byte[] id;
	private MifareKeyType(byte[] id) {
		this.id = id;
	}
	
	@Override
	public byte[] getBytes() {
		return id;
	}
}
