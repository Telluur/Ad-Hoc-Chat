package chat.network;

import java.util.HashMap;

import chat.gui.View;

public class TimeoutTimer extends Thread {
	private HashMap<Integer, Client> clients;
	private View view;
	
	public TimeoutTimer(HashMap<Integer, Client> clients, View view) {
		this.clients = clients;
		this.view = view;
	}

	@Override
	public void run() {
		while (true) {
			for (Client client : clients.values()) {
				if (System.currentTimeMillis() - client.getLastBro() > 30000) {
					System.out.println("Removing client!");
					view.addText(client.getId() + " has timed out.");
					clients.remove(client.getId());
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
