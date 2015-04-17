package io.github.derekstavis.devices.mifare;

import io.github.derekstavis.devices.IOAdapter;

import java.util.HashSet;
import java.util.Set;

public abstract class MifareModule extends Thread implements IOAdapter.Callback {
	Set<MifareEventListener> listeners;
	
	private static final int EVENT_LOOP_INTERVAL = 200;
	
	private IOAdapter commHandler;
	
	public MifareModule(IOAdapter adapter) {
		listeners = new HashSet<MifareEventListener>();
		
		commHandler = adapter;
		commHandler.addEventListener(this);
	}
	
	protected IOAdapter getIOHandler() {
		return commHandler;
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			proccessEventQueue();
			
			try {
				Thread.sleep(EVENT_LOOP_INTERVAL);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		
	}
	
	public void addEventListener(MifareEventListener listener) {
		listeners.add(listener);
	}
	
	public void removeEventListener(MifareEventListener listener) {
		listeners.remove(listener);
	}
	
	public void notifyEvent(MifareEvent e) {
		Set<MifareEventListener> copy = new HashSet<MifareEventListener>(listeners);
		for (MifareEventListener listener : copy) {
			listener.onMifareEvent(e);
		}
	}
	
	public abstract void proccessEventQueue();
	
	public abstract void requestSearch();
	public abstract void requestSectorLogin(int sectorAddress, MifareKey key);
	public abstract void requestDataBlockRead(int blockNumber);
	public abstract void requestDataBlockWrite(int blockNumber, byte[] block);
	public abstract void requestFirmwareVersion();
	
}
