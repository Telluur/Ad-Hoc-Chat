package chat.network;

public class ChatClient {
	
	public static void main(String[] args) {
		new ChatClient();
	}
	
	public ChatClient() {
		new NetworkController(0);
	}
}
