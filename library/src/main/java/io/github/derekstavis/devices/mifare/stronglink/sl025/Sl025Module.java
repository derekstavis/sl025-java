package io.github.derekstavis.devices.mifare.stronglink.sl025;

import io.github.derekstavis.devices.mifare.*;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class Sl025Module extends MifareModule {
	
	public static MifareModule connectTo(io.github.derekstavis.devices.IOAdapter io) {
		return new Sl025Module(io);
	}
	
	private final int BUFFER_CAPACITY = 65535;
	
	private ByteBuffer buffer;
	
	private Deque<Packet> responsePackets;
	private Deque<Packet> outputPackets;
	
	private Sl025Module(io.github.derekstavis.devices.IOAdapter io) {
		super(io);
		
		buffer = ByteBuffer.allocate(BUFFER_CAPACITY);
		
		responsePackets = new ArrayDeque<Packet>();
		outputPackets = new ArrayDeque<Packet>();
	}

	private MifareEvent.Type getEventType(ResponsePacket pkt) {
		System.out.println("Sl025Module: getEventType -> status = " + pkt.getStatus() + ", command = " + pkt.getCommand());
		
		switch (pkt.getStatus()) {
		case ADDRESS_OVERFLOW: 		return MifareEvent.Type.FAIL_ADDRESS;
		case FAIL_CHECKSUM: 		return MifareEvent.Type.FAIL_ADDRESS;
		case FAIL_DOWNLOAD_KEY: 	return MifareEvent.Type.FAIL_UNKNOWN;
		case FAIL_LOGIN:			return MifareEvent.Type.FAIL_LOGIN;
		case FAIL_READ:				return MifareEvent.Type.FAIL_READ;
		case FAIL_READ_AFTER_WRITE:	return MifareEvent.Type.FAIL_READ;
		case FAIL_WRITE:			return MifareEvent.Type.FAIL_WRITE;
		case NOT_AUTHENTICATED:		return MifareEvent.Type.FAIL_LOGIN;
		case NOT_VALUE_BLOCK:		return MifareEvent.Type.FAIL_ADDRESS;
		case NO_TAG:				return MifareEvent.Type.NO_CARDS;
		case SUCCESS:
			if (pkt.getCommand() == Command.WRITE_BLOCK) {
				return MifareEvent.Type.SUCCESS_WRITE;
			} else {
				return MifareEvent.Type.SUCCESS;
			}
		case SUCCESS_LOGIN:			return MifareEvent.Type.SUCCESS_LOGIN;
		case UNKNOWN_COMMAND:		return MifareEvent.Type.FAIL_UNKNOWN;
		default:					return MifareEvent.Type.FAIL_UNKNOWN;
		}
	}
		
	private void dispatchResponsePacket(Packet packet) {
		Logger.d("Packet arrived! %s", packet);
		
		ResponsePacket responsePacket = (ResponsePacket) packet;
		MifareEvent.Type eventType = getEventType(responsePacket);
		MifareEvent event = new MifareEvent(eventType);
		
		Logger.d("Sl025Module: Dispatching a response: " + responsePacket.getCommand().toString());
		
		switch (responsePacket.getCommand()) {
		case SELECT_CARD: 			event.attach(getMifareCard(responsePacket)); break;
		case GET_FIRMWARE_VERSION: 	event.attach(getModuleVersion(responsePacket)); break;
		default:					event.attach(responsePacket.getData()); break;
		}
		
		notifyEvent(event);
	}

	private String getModuleVersion(ResponsePacket responsePacket) {
		byte[] data = responsePacket.getData();
		return new String(data);
	}

	private MifareCard getMifareCard(ResponsePacket responsePacket) {
		byte[] data = responsePacket.getData();
		
		Logger.debugArray(data);
		
		if (data.length > 0) {
			byte[] uid = Arrays.copyOfRange(data, 0, data.length - 1);
			
			MifareType type = MifareTypes.fromByte(data[data.length - 1]);
			
			return new MifareCard(type, uid);
		}
		
		return null;
	}

	@Override
	public void requestSearch() {
		
		Packet packet = CommandPacket.fromCommand(Command.SELECT_CARD);
		outputPackets.add(packet);
		Logger.debugArray(packet.getBytes());
		
	}

	@Override
	public void requestSectorLogin(int sectorAddress, MifareKey key) {
		
		byte[] data = key.getBytes();
		byte[] bytes = new byte[data.length + 1];
		
		bytes[0] = ((byte) (sectorAddress & 0xFF));
		System.arraycopy(data, 0, bytes, 1, data.length);
		
		Packet packet = CommandPacket.fromCommand(Command.LOGIN_SECTOR, bytes);
		outputPackets.add(packet);
		
		Logger.debugArray(packet.getBytes());
		
	}

	@Override
	public void requestDataBlockRead(int blockNumber) {
		
		byte addr = ((byte) (blockNumber & 0xFF));
		
		Packet packet = CommandPacket.fromCommand(Command.READ_BLOCK, addr);
		outputPackets.add(packet);
		
		Logger.debugArray(packet.getBytes());
		
	}

	@Override
	public void requestDataBlockWrite(int blockNumber, byte[] block) {
		
		byte[] bytes = new byte[block.length + 1];
		
		bytes[0] = ((byte) (blockNumber & 0xFF));
		System.arraycopy(block, 0, bytes, 1, block.length);
		
		Packet packet = CommandPacket.fromCommand(Command.WRITE_BLOCK, bytes);
		outputPackets.add(packet);
		
		Logger.debugArray(packet.getBytes());
		
	}

	@Override
	public void requestFirmwareVersion() {
		
		Packet packet = CommandPacket.fromCommand(Command.GET_FIRMWARE_VERSION);
		outputPackets.add(packet);
		Logger.debugArray(packet.getBytes());
		
	}
	
	private void extractPackets() {
		int position = buffer.position();
		Packet pkt = null;
		
		if (buffer.position() > 0) {
			
			if (position < ResponsePacket.MIN_SIZE ) {
				return;
	    	}
	    	
	    	buffer.flip();
	    	buffer.mark();    	
	    	
			try {
				Logger.d("~> Trying to parse a ResponsePacket");
				pkt = ResponsePacket.fromByteBuffer(buffer);
				
				Logger.d("pkt = %s\n", pkt);
								
			} catch (IncompletePacketException e) {
				Logger.d("Found a incomplete ResponsePacket in buffer");
				
		    } catch (InvalidPacketException e) {
				Logger.d("Could not parse a ResponsePacket from buffer");

				Logger.d("Discarding a byte from the invalid buffer position.");
				
				buffer.reset();
				buffer.get();
				
				Logger.d("Buffer position after discarding: %d\n", buffer.position());
			}
			
			if (pkt != null && pkt.isValid()) {
				
				Logger.d("Valid packet enqueued for dispatching.");
				responsePackets.add(pkt);
				
				Logger.d("Buffer position after parsing: %d\n", buffer.position());
			}
			
			buffer.compact();
			
		}
	}
	
	private boolean dispatchPackets() {
		Packet packet;
			
		if (outputPackets.size() > 0) {
			packet = outputPackets.pollFirst();
			CommandPacket cmdPacket = (CommandPacket) packet;
			
			Logger.d("Sl025Module: Dispatching a request: " + cmdPacket.getCommand().toString());
			
			if (packet != null) {
				getIOHandler().writeBytes(packet.getBytes());
				return true;
			}
		}
		
		if (responsePackets.size() > 0) {
			packet = responsePackets.pollFirst();
			
			if (packet != null) {
				dispatchResponsePacket(packet);
				return true;
			}
		}
		
		return false;
	}
	
	public void onIOEvent() {
	
		byte bytes[] = getIOHandler().readBytes();
		
		if (bytes != null && bytes.length > 0) {
			
			synchronized (buffer) {
				buffer.put(bytes);
			}
			
			StringBuilder sb = new StringBuilder("TestRunner: [");
			for (byte b : bytes) {
				sb.append(String.format("%02X ", b));
			}
			sb.append("]");
			
			Logger.d(sb.toString());
			
		} else {
			Logger.d("Sl025Module: No bytes read!");
		}
	
	
	}
	
	public void proccessEventQueue() {
		
		synchronized (buffer) {
			if (dispatchPackets()) return;
		}
		
		synchronized (buffer) {
			extractPackets();
		}
		
	}

	
	
}
