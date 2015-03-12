package io.github.derekstavis;

import io.github.derekstavis.devices.mifare.MifareKey;
import io.github.derekstavis.devices.mifare.stronglink.sl025.MifareKeyType;

public class Constants {
	public static final boolean DEBUG = false;
	public static final String revision = "!repository_revision";
	
	public static MifareKey DEFAULT_KEY_A = MifareKey.get(MifareKeyType.KEY_A,
			new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 
						 (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
}
