package chat.objects;

import java.util.HashMap;

import chat.gui.AppView;
import chat.network.AckTimer;
import chat.network.Flag;
import chat.network.Packet;
import chat.network.Peer;

public class PrivateChat extends Chat {
	private Client client;
	private HashMap<Integer, Client> globalClients;

	public PrivateChat(Peer peer, int deviceNumber, int destination, AppView appView, HashMap<Integer, Client> globalClients) {
		super(peer, deviceNumber, destination, appView);
		this.globalClients = globalClients;
		client = new Client(destination, 0);
		view.addText(destination + " started a private chat!");
	}

	@Override
	public void onReceive(Packet packet) {
		if (packet.getFlag() == Flag.SYN) {
			// Send ACK back
			peer.send(new Packet(globalClients.size() - 1, deviceNumber, packet.getSource(), Flag.ACK, packet.getFlagNumber(), true, peer.getPacketId(), new byte[0]));
			if (client.getExtSyn() <= packet.getFlagNumber()) {
				// Message received, add this to GUI window.
				view.addText(packet.getSource(), new String(packet.getPayload()));
				// Update syn
				client.setSyn(packet.getFlagNumber() + 1);
			}
		} else if (packet.getFlag() == Flag.ACK) {
			AckTimer ackTimer = getAckTimer(client, packet.getFlagNumber());
			if (ackTimer != null) {
				ackTimer.stopRunning();
				ackTimers.remove(ackTimer);
			}
		}
	}

	@Override
	public void send(String text) {
		System.out.println("Sending to: " + destination);
		view.addText(deviceNumber, text);

		Packet packet = createPacket(text, globalClients.size() - 1);
		peer.send(packet);

		// Start ack timer.
		AckTimer ackTimer = new AckTimer(peer, packet, client, syn);
		ackTimer.start();
		ackTimers.add(ackTimer);

		syn++;
	}
}
