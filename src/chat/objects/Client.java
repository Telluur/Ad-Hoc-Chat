package chat.objects;

import java.util.ArrayList;

public class Client {
	private int id;
	private int extSyn;
	private long lastBro = 0;

	private ArrayList<Integer> receivedPackets = new ArrayList<>();

	public Client(int id, int extSyn) {
		this.id = id;
		this.extSyn = extSyn;
	}

	public void setSyn(int extSyn) {
		this.extSyn = extSyn;
	}

	public int getId() {
		return id;
	}

	public int getExtSyn() {
		return extSyn;
	}

	public void setLastBro(long lastBro) {
		this.lastBro = lastBro;
	}

	public long getLastBro() {
		return lastBro;
	}

	public void addPacket(Integer packetId) {
		receivedPackets.add(packetId);
	}

	public boolean gotPacket(Integer packetId) {
		return receivedPackets.contains(packetId);
	}
}
