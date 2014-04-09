package chat.network;

public class NetworkController {
	private int deviceNumber;
	private Peer peer;

	public NetworkController(int deviceNumber, Peer peer) {
		this.deviceNumber = deviceNumber;
	}

	public void onReceive(Packet packet) {
		if (packet.getDestination() != deviceNumber) {
			if (packet.getSource() != deviceNumber && packet.getHops() > 0) {
				peer.send(new Packet(packet.getHops() - 1, packet.getSource(), packet.getDestination(), packet.getFlag(), packet.getFlagNumber(), packet.getPayload()));
			}
		} else {
			// Accept packet
			if (packet.getFlag() == Flag.SYN) {
				// Message received, add this to GUI window.
				System.out.println(packet.getSource() + ": " + new String(packet.getPayload()));
			} else if (packet.getFlag() == Flag.ACK) {
				// Acknowledgement for message received.
				//TODO Add ack system.
			}
		}
	}
}
