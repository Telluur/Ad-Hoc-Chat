package chat.objects;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import chat.network.NetworkController;
import chat.network.Packet;

public class Peer implements Runnable {
	private NetworkController networkController;

	private InetAddress multicastAddress;
	private int port;
	private MulticastSocket multicastSocket;
	private int deviceNumber;

	private boolean running = true;

	private int packetId = 0;

	public Peer(NetworkController networkController, InetAddress multicastAddress, int port, int deviceNumber) {
		this.networkController = networkController;
		this.multicastAddress = multicastAddress;
		this.port = port;
		this.deviceNumber = deviceNumber;
		try {
			multicastSocket = new MulticastSocket(port);
			multicastSocket.joinGroup(multicastAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}

		new Broadcast().start();
	}

	public int getPacketId() {
		return packetId;
	}

	public void send(Packet packet) {
		try {
			if (packet.getSource() == deviceNumber) {
				packetId++;
			}
			multicastSocket.send(new DatagramPacket(packet.getBytes(), packet.length(), multicastAddress, port));
			if (packet.getFlag() != Flag.BRO) {
				System.out.println("[Out] " + packet.getHops() + " , " + packet.getDestination() + " , " + packet.getSource() + " , " + packet.getFlag() + " , " + packet.getFlagNumber() + " , "
						+ packet.isPrivateChat() + " , " + new String(packet.getPayload()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (running) {
			byte[] buffer = new byte[1000];
			DatagramPacket receive = new DatagramPacket(buffer, buffer.length);
			try {
				multicastSocket.receive(receive);
				Packet packet = new Packet(receive.getData());

				if (packet.getFlag() != Flag.BRO) {
					System.out.println("[In] " + packet.getHops() + " , " + packet.getDestination() + " , " + packet.getSource() + " , " + packet.getFlag() + " , " + packet.getFlagNumber() + " , "
							+ packet.isPrivateChat() + " , " + new String(packet.getPayload()));
				}

				networkController.onReceive(packet);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		multicastSocket.close();
		running = false;
	}

	class Broadcast extends Thread {
		public void run() {
			while (running) {
				try {
					int hops;
					if (networkController.getGlobalChat() != null) {
						hops = networkController.getGlobalChat().getClients().size() - 1;
					} else {
						hops = 0;
					}
					send(new Packet(hops, deviceNumber, 0, Flag.BRO, 0, false, packetId, new byte[0]));
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.out.println("Broadcast timer was interrupted!");
				}
			}
		}
	}
}
