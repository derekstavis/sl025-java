package io.github.derekstavis.devices;

import java.util.HashSet;
import java.util.Set;

public abstract class IOAdapter {
	
	private Set<Callback> listeners;
	
	public interface Callback {
		
		public void onIOEvent();
		
	}
	
	public IOAdapter() {
		listeners = new HashSet<IOAdapter.Callback>();
	}
	
	public abstract void reopen();
	public abstract void writeBytes(byte[] bytes);
	public abstract byte[] readBytes();
	
	public void addEventListener(Callback listener) {
		listeners.add(listener);
	}
	
	public void removeEventListener(Callback listener) {
		listeners.remove(listener);
	}
	
	protected void notifyEvent() {
		for (Callback listener : listeners) {
			listener.onIOEvent();
		}
	}
	
}
