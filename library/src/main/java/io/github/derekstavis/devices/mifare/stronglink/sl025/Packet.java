package io.github.derekstavis.devices.mifare.stronglink.sl025;

import java.nio.ByteBuffer;

public abstract class Packet {

	protected static final int MAX_SIZE = 255;
	
	private static final int INDEX_FRAME_LENGHT = 1;
	
	ByteBuffer buffer;
	
	
	/* --------------------- Protected Methods ----------------------- */
	
	protected Packet() {
		buffer = ByteBuffer.allocate(MAX_SIZE);
	}
	
	protected ByteBuffer getBuffer() {
		return buffer;
	}
	
	protected void closePacket() {
		byte checksum = calculateChecksum();
		
		buffer.put(checksum);
	}
	
	/* --------------------- Public Methods ----------------------- */
	
	public byte getChecksum() {
		return buffer.get(buffer.position() - 1);
	}
	
	public byte calculateChecksum() {
		byte checksum = 0;
		
		int lenght = getFrameLenght() + 1; // frame_len + command byte + lenght byte - checksum byte
		
		for (int i = 0; i < lenght; i++) {
			byte b = buffer.get(i);
			
			checksum ^= b;
		}
		
		return checksum;
	}
	
	public boolean isValid() {
		return getChecksum() == calculateChecksum();
	}
	
	public int getFrameLenght() {
		return (int) buffer.get(INDEX_FRAME_LENGHT);
	}
	
	public byte[] getBytes() {
		byte[] bytes = new byte[buffer.position()];
		
		for (int i = 0; i < buffer.position(); i++) {
			bytes[i] = buffer.get(i);
		}
		
		return bytes;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		
		for (int i = 0; i < buffer.position(); i++) {
			builder.append(String.format("%02X ", buffer.get(i)));
		}
		
		builder.append("]");
		
		return builder.toString();
	}
	
}
