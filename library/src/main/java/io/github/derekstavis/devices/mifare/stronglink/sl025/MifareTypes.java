package io.github.derekstavis.devices.mifare.stronglink.sl025;

import io.github.derekstavis.devices.mifare.MifareType;

public enum MifareTypes implements MifareType {

	MIFARE_1K_UID4      ((byte) 0x01, 1024, 4, "Mifare 1K"),
	MIFARE_1K_UID7      ((byte) 0x02, 1024, 7, "Mifare 1K"),
	MIFARE_ULTRALIGHT   ((byte) 0x03, 1024, 7, "Mifare UltraLight or NATG203"),
	MIFARE_4K_UID4      ((byte) 0x04, 4096, 4, "Mifare 4K"),
	MIFARE_4K_UID7      ((byte) 0x05, 4096, 7, "Mifare 4K"),
	DESFIRE             ((byte) 0x06, 1024, 7, "DesFire"),
	OTHER               ((byte) 0x0A, 1024, 4, "Other");
	
	private final byte type;
	private final int capacity, uidLenght;
	private final String title;
	
	private MifareTypes(byte type, int capacity, int uidLenght, String title) {
		this.type = type;
		this.capacity = capacity;
		this.uidLenght = uidLenght;
		this.title = title;
	}

	@Override
	public int getUIDLength() {
		return uidLenght;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}
	
	protected byte getTypeId() {
		return type;
	}
	
	public static MifareType fromByte(byte id) {
		for (MifareTypes type : values()) {
			if (type.getTypeId() == id) {
				return type;
			}
		}
		
		return OTHER;
	}
	
}
