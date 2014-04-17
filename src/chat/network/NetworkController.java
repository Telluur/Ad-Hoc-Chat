package chat.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;

import chat.objects.GlobalChat;
import chat.objects.Peer;
import chat.objects.PrivateChat;
import chat.gui.AppView;

public class NetworkController {

	public int deviceNumber;
	private Peer peer;

	private AppView appView;

	private TimeoutTimer timeoutTimer;

	private GlobalChat globalChat;

	// Hashmap of active private chats (Key: source)
	private HashMap<Integer, PrivateChat> privateChats = new HashMap<>();
	
	public static void main(String[] args) {
		new NetworkController();
	}

	public NetworkController() {
		InetAddress group;
		try {
			InetAddress inetAddress = getAddress();
			deviceNumber = inetAddress.getAddress()[3];

			group = InetAddress.getByName("226.1.2.3");
			peer = new Peer(this, group, 6789, deviceNumber);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		appView = new AppView(this);

		globalChat = new GlobalChat(peer, deviceNumber, 0, appView);

		timeoutTimer = new TimeoutTimer(globalChat.getClients(), globalChat.getView());
		timeoutTimer.start();
		
		new Thread(peer).start();
	}
	
	public int getDeviceNumber() {
		return deviceNumber;
	}

	public GlobalChat getGlobalChat() {
		return globalChat;
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

	public void startPrivateChat(int destination) {
		if (!privateChats.containsKey(destination)) {
			PrivateChat privateChat = new PrivateChat(peer, deviceNumber, destination, appView, globalChat.getClients());
			privateChats.put(destination, privateChat);
		}
	}

	public void onReceive(Packet packet) {
		if (packet.getSource() != deviceNumber) {
			globalChat.addClient(packet.getSource(), packet.getFlagNumber());
			if (!globalChat.getClients().get(packet.getSource()).gotPacket(packet.getPacketId())) {
				if (packet.getDestination() == 0 || packet.getDestination() == deviceNumber) {
					 // Handle normal group chat
					if (!packet.isPrivateChat()) {
						globalChat.onReceive(packet);
					} else {
						if (!privateChats.containsKey(packet.getSource())) {
							PrivateChat privateChat = new PrivateChat(peer, deviceNumber, packet.getSource(), appView, globalChat.getClients());
							privateChats.put(packet.getSource(), privateChat);
						}
						privateChats.get(packet.getSource()).onReceive(packet);
					}
				}
				// Check if you were the only destination, no need to resend in that case.
				if (packet.getHops() > 0 && packet.getDestination() != deviceNumber) {
					peer.send(new Packet(packet.getHops() - 1, packet.getSource(), packet.getDestination(), packet.getFlag(), packet.getFlagNumber(), packet.isPrivateChat(), packet.getPacketId(), packet.getPayload()));
				}
			}
		}
	}
}