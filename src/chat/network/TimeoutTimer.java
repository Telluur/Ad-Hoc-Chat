package chat.network;

import java.util.HashMap;
import java.util.Iterator;

import chat.objects.Client;

import chat.gui.ChatView;

public class TimeoutTimer extends Thread {
	private HashMap<Integer, Client> clients;
	private ChatView view;
	
	public TimeoutTimer(HashMap<Integer, Client> clients, ChatView view) {
		this.clients = clients;
		this.view = view;
	}

	@Override
	public void run() {
		while (true) {
			Iterator<Client> iterator = clients.values().iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				if (System.currentTimeMillis() - client.getLastBro() > 5000) {
					view.addText(client.getId() + " has timed out.");
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
