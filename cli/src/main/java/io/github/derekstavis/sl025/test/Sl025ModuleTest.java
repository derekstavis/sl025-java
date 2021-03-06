package io.github.derekstavis.sl025.test;

import io.github.derekstavis.devices.IOAdapter;
import io.github.derekstavis.devices.mifare.MifareCard;
import io.github.derekstavis.devices.mifare.MifareEvent;
import io.github.derekstavis.devices.mifare.MifareEventListener;
import io.github.derekstavis.devices.mifare.MifareModule;
import io.github.derekstavis.devices.mifare.stronglink.sl025.*;
import jssc.SerialPort;
import jssc.SerialPortException;

import java.nio.ByteBuffer;
import java.util.Arrays;

class Sl025ModuleTest {

    public static void onlineTest(String port) {
		IOAdapter io = null;
		MifareModule module = null;

		try {
			io = SerialPortIOAdapter.get(port, SerialPort.BAUDRATE_115200,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			module = Sl025Module.connectTo(io);
			module.start();

		} catch (SerialPortException e) {
			e.printStackTrace();
		}

		MifareEventListener el = new MifareEventListener() {

			@Override
			public void onMifareEvent(MifareEvent e) {
				debug("TestRunner: Response from SL025M:");
				debug("TestRunner:   ~> Event Type:\t%s", e.getType().toString());

				if (e.getObject() instanceof MifareCard) {
					MifareCard card = (MifareCard) e.getObject();

					debug("TestRunner:   ~> Card ID:\t\t%s", card.getUid());
					debug("TestRunner:   ~> Card Type:\t%s", card.getType()
							.toString());
				} else if (e.getObject() instanceof String) {
					String firmwareVersion = (String) e.getObject();
					debug("TestRunner:   ~> Firmware:\t\t%s", firmwareVersion);
				} else if (e.getObject() instanceof byte[]) {
					debug("TestRunner:   ~> Payload:\t\t%s", Arrays.toString((byte[]) e.getObject()));
				} else {
					debug("TestRunner:   ~> Attachent:\t%s", e.getObject());
				}
			}

		};

		if (module != null) {
			module.addEventListener(el);

			module.requestFirmwareVersion();
			module.requestSearch();
			
			debug("TestRunner: Sending a %s request", Command.LOGIN_SECTOR);
			module.requestSectorLogin(0x00, Constants.DEFAULT_KEY_A);
			module.requestDataBlockRead(0x01);
			
			module.requestDataBlockWrite(0x01, new byte[16]);
			
			module.requestDataBlockRead(0x01);

		}

	}

	public static void offlineTest() {

		debug("TestRunner: Running packet assembly tests");

		debug("TestRunner: --------------------------------------------");
		debug("TestRunner: -------------- Request Packets -------------");
		debug("TestRunner: --------------------------------------------");
		
		CommandPacket commandPacket;
		
		debug("TestRunner: Sending a %s request", Command.SELECT_CARD);
		commandPacket = CommandPacket.fromCommand(Command.SELECT_CARD);
		debugArray(commandPacket.getBytes());
		debugPacket(commandPacket);
		
		debug("TestRunner: Sending a %s request", Command.LOGIN_SECTOR);
		commandPacket = CommandPacket.fromCommand(Command.LOGIN_SECTOR, Constants.DEFAULT_KEY_A.getBytes());
		debugArray(commandPacket.getBytes());
		debugPacket(commandPacket);

		debug("TestRunner: --------------------------------------------");
		debug("TestRunner: ------------- Response Packets -------------");
		debug("TestRunner: --------------------------------------------");

		byte[] bytes = new byte[] { (byte) 0xBD, (byte) 0x0C, (byte) 0xF0,
				(byte) 0x00, (byte) 0x53, (byte) 0x4C, (byte) 0x30,
				(byte) 0x32, (byte) 0x35, (byte) 0x2D, (byte) 0x31,
				(byte) 0x2E, (byte) 0x32, (byte) 0x69 };

		ResponsePacket packet = ResponsePacket.fromBytes(bytes);
		
		debugPacket(packet);

		try {
			packet = ResponsePacket.fromByteBuffer(ByteBuffer.wrap(bytes));
		} catch (IncompletePacketException e) {
			e.printStackTrace();
		} catch (InvalidPacketException e) {
			e.printStackTrace();
		}

		debugPacket(packet);
	}

	private static void debug(String fmt, Object... args) {
		String message = fmt.concat("\n");
		System.out.printf(message, args);
	}

	private static void debugPacket(Packet commandPacket) {
		debug("TestRunner: calculateChecksum\t= 0x%02X",
				commandPacket.calculateChecksum());
		debug("TestRunner: getChecksum\t\t= 0x%02X",
				commandPacket.getChecksum());
		debug("TestRunner: isValid\t\t= %s",
				commandPacket.isValid() ? "YES" : "NO");
	}

	public static void debugArray(byte[] bytes) {

		StringBuilder sb = new StringBuilder("DebugArray: Array -> [");
		for (byte b : bytes) {
			sb.append(String.format("%02X ", b));
		}
		sb.append("]");

		debug(sb.toString());
	}

}
