package chat.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Peer implements Runnable {
	InetAddress multicastAddress;
	int port;
	MulticastSocket multicastSocket;

	public Peer(InetAddress multicastAddress, int port) {
		this.multicastAddress = multicastAddress;
		this.port = port;
		try {
			multicastSocket = new MulticastSocket(port);
			multicastSocket.joinGroup(multicastAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(Packet packet){
		try {
			multicastSocket.send(new DatagramPacket(packet.getBytes(), packet.length(), multicastAddress, port));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void run(){
		while (true) {
			byte[] buffer = new byte[1000];
			DatagramPacket receive = new DatagramPacket(buffer, buffer.length);
			try {
				multicastSocket.receive(receive);
				if (receive.getAddress() != multicastSocket.getLocalAddress()) {
					System.out.println(new Packet(receive.getData()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void close(){
		multicastSocket.close();
	}
}
