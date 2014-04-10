package chat.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetworkController {
	
	
	private int deviceNumber;
	private Peer peer;

	public NetworkController(int deviceNumber) {
		InetAddress group;
		try {
			InetAddress inetAddress = getAddress();
			deviceNumber = inetAddress.getAddress()[3];
			
			group = InetAddress.getByName("226.1.2.3");
			Peer peer = new Peer(group, 6789, deviceNumber);
			new Thread(peer).start();
			
			//Test message
			peer.send(new Packet(5, deviceNumber, 0, Flag.SYN, 0, "dit is een message van client 1".getBytes()));			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}


		this.deviceNumber = deviceNumber;
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