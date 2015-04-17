package io.github.derekstavis.devices.mifare;

public class MifareEvent {

	public static enum Type {
		NO_CARDS,
		SUCCESS,
		SUCCESS_WRITE,
		SUCCESS_LOGIN,
		FAIL_ADDRESS,
		FAIL_UNKNOWN,
		FAIL_LOGIN,
		FAIL_READ,
		FAIL_WRITE
	}
	
	private Type type = null;
	private Object object = null;
	
	public MifareEvent(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public void attach(Object object) {
		this.object = object;
	}
	
	public Object getObject() {
		return object;
	}
	
}