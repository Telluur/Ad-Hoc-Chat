package chat.network;

import chat.objects.Client;
import chat.objects.Peer;

public class AckTimer extends Thread {
	private Peer peer;
	private Packet packet;
	private Client client;
	private int syn;

	private int resendCount = 0;
	private boolean keepGoing = true;

	private final int MAX_RETRIES = 6;

	public AckTimer(Peer peer, Packet packet, Client client, int syn) {
		this.peer = peer;
		this.packet = packet;
		this.client = client;
		this.syn = syn;
	}

	public Client getClient() {
		return client;
	}

	public int getSyn() {
		return syn;
	}

	public void stopRunning() {
		keepGoing = false;
	}

	@Override
	public void run() {
		// Wait 5 seconds before first resend.
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		while (keepGoing) {
			resendCount++;

			peer.send(new Packet(packet.getHops(), packet.getSource(), client.getId(), packet.getFlag(), packet.getFlagNumber(), packet.isPrivateChat(), resendCount, packet.getPayload()));

			if (resendCount >= MAX_RETRIES) {
				keepGoing = false;
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public String toString() {
		return "client " + client.getId() + " , syn " + syn;
	}
}
