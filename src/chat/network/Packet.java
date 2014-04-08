package chat.network;

import java.util.Arrays;

public class Packet {
	byte[] data;
	int id;
	
	public Packet(byte[] data) {
		this.data = data;
	}
	
	public Packet(int hops, int validationNumber, byte[] payload) {
		data = new byte[payload.length + 2];
		data[0] = (byte) hops;
		data[1] = (byte) validationNumber;
		System.arraycopy(payload, 0, data, 2, 3);
		System.out.println(new String(data));
		System.out.println(new String(payload) + " : "  + new String(data));
	}
	
	public int getHops() {
		return data[0];
	}
	
	public int getValidationNumber() {
		return data[1];
	}
	
	public byte[] getPayload() {
		return Arrays.copyOfRange(data, 2, data.length);
	}
	
	public byte[] getBytes() {
		return data;
	}
	
	public int length() {
		return data.length;
	}
}

//number of hops
//syn/acks