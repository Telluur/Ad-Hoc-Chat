package chat.network;

public enum Flag {
	BRO (0),
	SYN (1),
	ACK (2);
	
	private int number;
	
	private Flag (int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
}
