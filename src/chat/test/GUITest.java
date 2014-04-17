package chat.test;

import chat.gui.AppView;
import chat.gui.ChatView;

public class GUITest {

	public static void main(String[] args) {
		new GUITest();
	}

	public GUITest() {
		setup();
	}
	
	public void setup() {
		AppView appView = new AppView(null);
		appView.addClient("1");
		appView.addClient("2");
		appView.addClient("3");
		ChatView global = appView.addChatWindow("global", null);
		ChatView testChat = appView.addChatWindow("testtab", null);
		global.addText(0, "Global test!");
		testChat.addText(1, "Test chat!");
		appView.removeClient("2");
	}
}
