package io.github.derekstavis.sl025.test;

import io.github.derekstavis.devices.IOAdapter;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

class SerialPortIOAdapter extends IOAdapter implements SerialPortEventListener {
	private SerialPort serial;
	
	public static SerialPortIOAdapter get(String name, int baudRate, int dataBits, int stopBits, int parity) throws SerialPortException {
		return new SerialPortIOAdapter(name, baudRate, dataBits, stopBits, parity);
	}
	
	private SerialPortIOAdapter(String portName, int baudRate, int dataBits, int stopBits, int parity) throws SerialPortException {
		serial = new SerialPort(portName);
		serial.openPort();
		serial.setParams(baudRate, dataBits, stopBits, parity);
		serial.addEventListener(this);
	}
	
	@Override
	public void reopen() {
		try {
			
			if (serial != null && serial.isOpened()) {
				serial.closePort();
			}
		
			serial.openPort();
			
		} catch (SerialPortException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeBytes(byte[] bytes) {
		
		if (serial.isOpened()) {
			try {
				serial.writeBytes(bytes);
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public byte[] readBytes() {
		
		if (serial.isOpened()) {
			try {
				return serial.readBytes();
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
		
		return null;
		
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		notifyEvent();
	}

}
