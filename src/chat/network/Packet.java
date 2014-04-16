package chat.network;

import java.util.Arrays;

public class Packet {
	byte[] data;

	public Packet(byte[] data) {
		this.data = data;
	}
	
	public Packet(int hops, int source, int destination, Flag flag, int flagNumber, boolean privateChat, int packetId, byte[] payload) {
		data = new byte[payload.length + 7];
		data[0] = (byte) hops;
		data[1] = (byte) source;
		data[2] = (byte) destination;
		data[3] = (byte) flag.getNumber();
		data[4] = (byte) flagNumber;
		data[5] = (byte) (privateChat ? 1 : 0);
		data[6] = (byte) packetId;
		System.arraycopy(payload, 0, data, 7, payload.length);
	}

	// x & 0x01 != 0
	// 0x02
	// 0x04
	// 0x08
	// 0x10
	// 0x20
	// 0x30
	// 0x40

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

	public boolean isPrivateChat() {
		return (data[5] & 0x01) != 0;
	}

	public int getPacketId() {
		return data[6];
	}
	
	public byte[] getPayload() {
		return Arrays.copyOfRange(data, 7, data.length);
	}
}

// number of hops
// syn/acks