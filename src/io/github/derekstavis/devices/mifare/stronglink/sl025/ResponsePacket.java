package io.github.derekstavis.devices.mifare.stronglink.sl025;

import io.github.derekstavis.util.Log;

import java.nio.ByteBuffer;


public class ResponsePacket extends Packet {
	
	protected static final byte PREAMBLE = (byte) 0xBD;
	
	private static final int INDEX_PREAMBLE = 0;
	private static final int INDEX_LENGHT 	= 1;
	private static final int INDEX_COMMAND 	= 2;
	private static final int INDEX_STATUS 	= 3;
	private static final int INDEX_DATA 	= 4;
	
	public static final int MIN_SIZE = 5;
	
	public static ResponsePacket fromBytes(byte... data) {
		return new ResponsePacket(data);
	}
	
	public static ResponsePacket fromByteBuffer(ByteBuffer buffer)
	throws InvalidPacketException, IncompletePacketException {
		Log.d("ResponsePacket: Trying to create a packet from a %d bytes buffer\n", buffer.limit());
		
		if (buffer != null && buffer.limit() >= MIN_SIZE) {
			
			byte preamble 	= buffer.get(INDEX_PREAMBLE);
			int frameLenght = buffer.get(INDEX_LENGHT);
			byte command 	= buffer.get(INDEX_COMMAND);
			
			Log.d(String.format("ResponsePacket: preamble=%02X, frameLenght=%02X, command=%02X",
					preamble, frameLenght, command));
			
			boolean canParse = true;
			
			canParse &= (PREAMBLE == preamble);
			canParse &= (Command.fromByte(command) != null);
			
			Log.d(String.format("ResponsePacket: Can Parse? %s", (canParse ? "YES" : "NO") ));

			if (canParse) {

				frameLenght += 2; // +2 because of preamble and packet size bytes

				if (buffer.limit() < frameLenght) {
					throw new IncompletePacketException(
						String.format("Packet declared as %d bytes long, but buffer have only %d bytes",
							          frameLenght, buffer.limit()));
				}
				
				byte[] packetBytes = new byte[frameLenght];
				
				buffer.get(packetBytes, 0, frameLenght);
				
				return ResponsePacket.fromBytes(packetBytes);
			}
			
		}
	
		throw new InvalidPacketException();
	}
	
	protected ResponsePacket(byte... data) {
		getBuffer().put(data);
	}
	
	public Command getCommand() {
		return Command.fromByte(buffer.get(INDEX_COMMAND));
	}
	
	public byte[] getData() {
		int dataLenght = getFrameLenght() - INDEX_STATUS;
		if (dataLenght <= 0) return new byte[0];
		
		byte[] bytes = new byte[dataLenght];
		
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = getBuffer().get(i + INDEX_DATA);
		}
		
		return bytes;
	}
	
	public Status getStatus() {
		return Status.fromByte(buffer.get(INDEX_STATUS));
	}
	
}
