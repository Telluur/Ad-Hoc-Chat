package chat.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import chat.gui.View;

public class NetworkController {

	private int deviceNumber;
	private Peer peer;
	
	private TimeoutTimer timeoutTimer;

	private View view;

	private HashMap<Integer, Client> clients = new HashMap<>();
	private HashMap<Integer, AckTimer> ackTimers = new HashMap<>();

	public static void main(String[] args) {
		new NetworkController();
	}

	public NetworkController() {
		this.view = new View(this);
		InetAddress group;
		try {
			InetAddress inetAddress = getAddress();
			deviceNumber = inetAddress.getAddress()[3];

			group = InetAddress.getByName("226.1.2.3");
			peer = new Peer(this, group, 6789, deviceNumber);
			new Thread(peer).start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		timeoutTimer = new TimeoutTimer(clients, view);
		timeoutTimer.start();
	}

	private InetAddress getAddress() {
		Enumeration<NetworkInterface> networkInterfaces;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
						return address;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void onReceive(Packet packet) {
		if (packet.getSource() != deviceNumber) {
			if (packet.getDestination() == 0 || packet.getDestination() == deviceNumber) {
				// Accept packet
				Client client;
				if (clients.containsKey(packet.getSource())) {
					client = clients.get(packet.getSource());
				} else {
					client = new Client(packet.getSource(), packet.getFlagNumber());
					clients.put(packet.getSource(), client);
					view.addText(packet.getSource() + " connected!");
				}
				client.setLastBro(System.currentTimeMillis());
				
				if (packet.getFlag() == Flag.BRO) {
					// Client has already been added & timer has been updated.
				} else if (packet.getFlag() == Flag.SYN) {
					if (clients.get(packet.getSource()).getSyn() <= packet.getFlagNumber()) {
						// Message received, add this to GUI window.
						view.addText(packet.getSource(), new String(packet.getPayload()));
						// Update syn
						clients.get(packet.getSource()).setSyn(packet.getFlagNumber() + 1);
						// Send ACK back
						peer.send(new Packet(5, deviceNumber, packet.getSource(), Flag.ACK, packet.getFlagNumber(), new byte[0]));
					}
				} else if (packet.getFlag() == Flag.ACK) {
					System.out.println("Got ack! " + packet.getFlagNumber());
					//TODO Loopt vast bij deze if
					if (ackTimers.containsKey(packet.getFlagNumber())) {
						AckTimer ackTimer = ackTimers.get(packet.getFlagNumber());
						ackTimer.addAck(packet.getSource());
						System.out.println("Added ack!");
						if (ackTimer.gotAllAcks()) {
							System.out.println("Got all acks, removed timer!");
							ackTimers.remove(packet.getFlagNumber());
						}
					}
					// Acknowledgement for message received.
					// TODO Add ack system.
					// TODO Add time before message
				}
			}
			//Check if you were the only destination, no need to resend in that case.
			if (packet.getHops() > 0 && packet.getDestination() != deviceNumber) {
				peer.send(new Packet(packet.getHops() - 1, packet.getSource(), packet.getDestination(), packet.getFlag(), packet.getFlagNumber(), packet.getPayload()));
			}
		}
	}

	public void send(String text) {
		view.addText(deviceNumber, text);
		Packet packet = new Packet(5, deviceNumber, 0, Flag.SYN, peer.getSyn(), text.getBytes());
		peer.send(packet);
		
		//Start ack timer.
		if (clients.size() > 0) {
			System.out.println("Starting ack timer!");
			AckTimer ackTimer = new AckTimer(peer, packet, clients);
			ackTimer.start();
			ackTimers.put(peer.getSyn(), ackTimer);
		}
	}
}