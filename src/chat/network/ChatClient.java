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
			peer.send(new Packet(0 , 0, "dit is die message van een andere".getBytes()));
			new Thread(peer).start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
