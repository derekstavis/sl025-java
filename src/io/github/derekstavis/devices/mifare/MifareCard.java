package io.github.derekstavis.devices.mifare;

import java.io.Serializable;

public class MifareCard implements Serializable {
	private static final long serialVersionUID = 2016840882397460652L;
	
	private byte[] uid;
	private MifareType type;
	
	public MifareCard(MifareType type, byte[] uid) {
		this.uid = uid;
		this.type = type;
	}
	
	public String getUid() {
		StringBuilder b = new StringBuilder();
		
		for (byte bt : uid) {
			b.append(String.format("%02X", bt));
		}
		
		return b.toString();
	}
	
	public MifareType getType() {
		return type;
	}
}
