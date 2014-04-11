package chat.network;

import java.util.Arrays;

public class Packet {
	byte[] data;
	
	public Packet(byte[] data) {
		this.data = data;
	}
	
	public Packet(int hops, int source, int destination, Flag flag, int flagNumber, byte[] payload) {
		data = new byte[payload.length + 5];
		data[0] = (byte) hops;
		data[1] = (byte) source;
		data[2] = (byte) destination;
		data[3] = (byte) flag.getNumber();
		data[4] = (byte) flagNumber;
		System.arraycopy(payload, 0, data, 5, payload.length);
	}
	
	public byte[] getBytes() {
		return data;
	}
	
	public int length() {
		return data.length;
	}
	
	public int getHops() {
		return data[0];
	}
	
	public int getSource() {
		return data[1];
	}
	
	public int getDestination() {
		return data[2];
	}
	
	public Flag getFlag() {
		return Flag.values()[data[3]];
	}
	
	public int getFlagNumber() {
		return data[4];
	}
	
	public byte[] getPayload() {
		return Arrays.copyOfRange(data, 5, data.length);
	}
}

//number of hops
//syn/acks