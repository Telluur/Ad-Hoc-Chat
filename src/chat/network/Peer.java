package chat.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Peer implements Runnable {
	private NetworkController networkController;
	
	private InetAddress multicastAddress;
	private int port;
	private MulticastSocket multicastSocket;
	private int deviceNumber;

	private boolean running = true;
	
	private int syn = 0;

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
	
	public int getSyn() {
		return syn;
	}

	public void send(Packet packet) {
		try {
			if (packet.getSource() == deviceNumber) {
				syn++;
			}
			multicastSocket.send(new DatagramPacket(packet.getBytes(), packet
					.length(), multicastAddress, port));
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
				networkController.onReceive(new Packet(receive.getData()));
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
					send(new Packet(5, deviceNumber, 0, Flag.BRO, 0,
							new byte[0]));
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					System.out.println("Broadcast timer was interrupted!");
				}
			}
		}
	}
}
