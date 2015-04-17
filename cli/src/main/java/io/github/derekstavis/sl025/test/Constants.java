package io.github.derekstavis.sl025.test;

import io.github.derekstavis.devices.mifare.MifareKey;
import io.github.derekstavis.devices.mifare.stronglink.sl025.MifareKeyType;

class Constants {
	public static MifareKey DEFAULT_KEY_A = MifareKey.get(MifareKeyType.KEY_A,
			new byte[] { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 
						 (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
}
