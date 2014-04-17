package chat.network;

import java.util.HashMap;
import java.util.Iterator;

import chat.objects.Client;

import chat.gui.AppView;
import chat.gui.ChatView;

public class TimeoutTimer extends Thread {
	private HashMap<Integer, Client> clients;
	private AppView view;
	private ChatView globalChat;
	
	public TimeoutTimer(HashMap<Integer, Client> clients, AppView view, ChatView globalChat) {
		this.clients = clients;
		this.view = view;
		this.globalChat = globalChat;
	}

	@Override
	public void run() {
		while (true) {
			Iterator<Client> iterator = clients.values().iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				if (System.currentTimeMillis() - client.getLastBro() > 5000) {
					globalChat.addText(client.getId() + " has timed out.");
					view.removeClient(Integer.toString(client.getId()));
					iterator.remove();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
