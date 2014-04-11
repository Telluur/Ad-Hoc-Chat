package chat.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AckTimer extends Thread {
	private boolean gotAllAcks = false;
	private List<Integer> receivedAcks = new ArrayList<>();

	private Peer peer;
	private Packet packet;
	private HashMap<Integer, Client> clients;

	public AckTimer(Peer peer, Packet packet, HashMap<Integer, Client> clients) {
		this.peer = peer;
		this.packet = packet;
		this.clients = clients;
	}

	public boolean gotAllAcks() {
		return gotAllAcks;
	}

	public void addAck(int source) {
		receivedAcks.add(source);

		for (Client client : clients.values()) {
			if (!receivedAcks.contains(client.getId())) {
				break;
			}
			gotAllAcks = true;
		}
	}

	@Override
	public void run() {
		while (!gotAllAcks) {
			for (Client client : clients.values()) {
				//Also check for received acks here in case a client disconnected before sending an ack.
				boolean resend = false;
				if (!receivedAcks.contains(client.getId())) {
					System.out.println("Resending..." + packet.getHops() + " , " + packet.getSource() + " , "  + client.getId());
					peer.send(new Packet(packet.getHops(), packet.getSource(), client.getId(), packet.getFlag(), packet.getFlagNumber(), packet.getPayload()));
					resend = true;
				}
				gotAllAcks = !resend;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
