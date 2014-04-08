package chat.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class NetworkController {

	public static void main(String[] args) {
		new NetworkController();
	}

	public NetworkController() {
		setup();
	}

	private void setup() {
		// join a Multicast group and send the group salutations
		String msg = "Hello";
		try {
			//226.1.2.3
			InetAddress group = InetAddress.getByName("228.5.6.7");
			MulticastSocket s = new MulticastSocket(6789);
			s.joinGroup(group);
			DatagramPacket hi = new DatagramPacket(msg.getBytes(),
					msg.length(), group, 6789);
			s.send(hi);
			// get their responses!
			byte[] buf = new byte[1000];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);
			if (recv.getAddress() != s.getLocalAddress()) {
				
			}
			System.out.println("Receiving...");
			s.receive(recv);
			System.out.println("Received: " + new String(recv.getData()) + " , size: "  + recv.getLength());
			// Leave the group
			s.leaveGroup(group);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
