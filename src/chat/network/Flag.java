package chat.network;

public enum Flag {
	SYN (0),
	ACK (1);
	
	private int number;
	
	private Flag (int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
}
