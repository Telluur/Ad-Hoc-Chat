package chat.network;

public class Client {
	private int id;
	private int syn;
	private long lastBro = 0;
	
	public Client(int id, int syn) {
		this.id = id;
		this.syn = syn;
	}
	
	public void setSyn(int syn) {
		this.syn = syn;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSyn() {
		return syn;
	}
	
	public void setLastBro(long lastBro) {
		this.lastBro = lastBro;
	}
	
	public long getLastBro() {
		return lastBro;
	}
}
