package io.github.derekstavis.devices.mifare.stronglink.sl025;

import java.nio.ByteBuffer;

public class CommandPacket extends Packet {
	
	protected static final byte PREAMBLE = (byte) 0xBA;
	
	private static final int INDEX_COMMAND = 2;
	
	public static final int MIN_SIZE = 4;
	
	public static CommandPacket fromCommand(Command cmd, byte... data) {
		return new CommandPacket(cmd, data);
	}
	
	protected CommandPacket(Command cmd, byte... data) {
		ByteBuffer buffer = getBuffer();
		
		byte packetLenght = (byte) (MIN_SIZE + data.length - 2); //command and length bytes doesn't count
		
		buffer.put(PREAMBLE);
		buffer.put(packetLenght);	
		buffer.put(cmd.getByte());
		
		if (data.length > 0) {
			buffer.put(data);
		}
		
		Logger.d(String.format("ResponsePacket: preamble=%02X, frameLenght=%02X, command=%02X",
				PREAMBLE, packetLenght, cmd.getByte()));
		
		closePacket();
	}
	
	@Override
	public boolean isValid() {
		return buffer.position() >= MIN_SIZE &&
			   super.isValid();
	}
	
	public Command getCommand() {
		return Command.fromByte(buffer.get(INDEX_COMMAND));
	}
	
	
}