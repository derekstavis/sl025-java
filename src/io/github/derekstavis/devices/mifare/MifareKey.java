package io.github.derekstavis.devices.mifare;

public class MifareKey {
	
	public static interface Type {
		public byte[] getBytes();
	}
	
	private Type keyType;
	private byte[] bytes;
	
	public static MifareKey get(Type type, byte[] bytes) {
		return new MifareKey(type, bytes);
	}
	
	private MifareKey(Type type, byte[] bytes) {
		this.keyType = type;
		this.bytes = bytes;
	}
	
	public byte[] getBytes() {
		int size = bytes.length + keyType.getBytes().length;
		byte[] keyTypeId = keyType.getBytes();
		byte[] mBytes = new byte[size];
		
		System.arraycopy(keyTypeId, 0, mBytes, 0, keyTypeId.length);
		System.arraycopy(bytes, 0, mBytes, keyTypeId.length, bytes.length);
		
		return mBytes;
	}
	
	public Type getType() {
		return keyType;
	}
	
}
