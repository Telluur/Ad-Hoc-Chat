package chat.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ChatClient {
	
	public static void main(String[] args) {
		new ChatClient();
	}
	
	public ChatClient() {
		//226.1.2.3
		InetAddress group;
		try {
			group = InetAddress.getByName("228.5.6.7");
			Peer peer = new Peer(group, 6789);
			new Thread(peer).start();
			new NetworkController(0, peer);
			
			peer.send(new Packet(0, 1, 0, Flag.SYN, 0, "dit is een message van client 1".getBytes()));			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
