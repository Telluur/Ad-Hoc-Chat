package chat.objects;

import java.util.HashMap;

import chat.gui.AppView;
import chat.network.AckTimer;
import chat.network.Flag;
import chat.network.Packet;
import chat.network.Peer;

public class GlobalChat extends Chat {
	// Hashmap of connected clients. Key: client devicenumber
	HashMap<Integer, Client> clients = new HashMap<>();

	public GlobalChat(Peer peer, int deviceNumber, int destination, AppView appView) {
		super(peer, deviceNumber, destination, appView);
	}
	
	public HashMap<Integer, Client> getClients() {
		return clients;
	}

	@Override
	public void onReceive(Packet packet) {
		// Accept packet
		Client client = clients.get(packet.getSource());
		client.setLastBro(System.currentTimeMillis());

		if (packet.getFlag() == Flag.BRO) {
			// Client has already been added & timer has been updated.
		} else if (packet.getFlag() == Flag.SYN) {
			// Send ACK back
			peer.send(new Packet(clients.size() - 1, deviceNumber, packet.getSource(), Flag.ACK, packet.getFlagNumber(), false, peer.getPacketId(), new byte[0]));
			if (clients.get(packet.getSource()).getExtSyn() <= packet.getFlagNumber()) {
				// Message received, add this to GUI window.
				view.addText(packet.getSource(), new String(packet.getPayload()));
				// Update syn
				clients.get(packet.getSource()).setSyn(packet.getFlagNumber() + 1);
			}
		} else if (packet.getFlag() == Flag.ACK) {
			AckTimer ackTimer = getAckTimer(client, packet.getFlagNumber());
			if (ackTimer != null) {
				ackTimer.stopRunning();
				ackTimers.remove(ackTimer);
			}
			// TODO Add time before message
		}
	}
	
	public void addClient(int source, int flagNumber) {
		Client client;
		if (clients.containsKey(source)) {
			client = clients.get(source);
		} else {
			client = new Client(source, flagNumber);
			clients.put(source, client);
			appView.addClient(Integer.toString(source));
			view.addText(source + " connected!");
		}
	}

	@Override
	public void send(String text) {
		view.addText(deviceNumber, text);
		
		Packet packet = createPacket(text, clients.size() - 1);
		peer.send(packet);
		
		// Start ack timer.
		for (Client client : clients.values()) {
			AckTimer ackTimer = new AckTimer(peer, packet, client, syn);
			ackTimer.start();
			ackTimers.add(ackTimer);
		}
		
		syn++;
	}
}
