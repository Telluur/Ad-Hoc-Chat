package chat.objects;

import java.util.ArrayList;
import java.util.List;

import chat.gui.AppView;
import chat.gui.ChatView;
import chat.network.AckTimer;
import chat.network.Flag;
import chat.network.Packet;
import chat.network.Peer;

public abstract class Chat {	
	protected int destination = 0;
	protected int deviceNumber;
	protected Peer peer;
	protected AppView appView;
	protected ChatView view;
	
	protected int syn = 0;
	
	// Hashmap of running ack timers.
	protected List<AckTimer> ackTimers = new ArrayList<AckTimer>();
	
	public Chat(Peer peer, int deviceNumber, int destination, AppView appView){
		this.peer = peer;
		this.deviceNumber = deviceNumber;
		this.destination = destination;
		this.appView = appView;
		if (destination == 0) {
			this.view = appView.addChatWindow("Global", this); 
		} else {
			this.view = appView.addChatWindow("Private Chat with " + destination, this); 
		}
	}
	
	public ChatView getView() {
		return view;
	}
	
	public abstract void onReceive(Packet packet);
	
	public abstract void send(String text);
	
	protected Packet createPacket (String text, int hops) {
		return new Packet(hops, deviceNumber, destination, Flag.SYN, syn, (this instanceof PrivateChat), peer.getPacketId(), text.getBytes());
	}
	
	protected AckTimer getAckTimer (Client client, int syn) {
		for (AckTimer ackTimer : ackTimers) {
			if (ackTimer.getClient() == client && ackTimer.getSyn() == syn) {
				return ackTimer;
			}
		}
		return null;
	}
}
