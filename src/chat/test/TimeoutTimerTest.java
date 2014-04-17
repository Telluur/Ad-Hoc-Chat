package chat.test;

import java.util.HashMap;

import chat.gui.AppView;
import chat.gui.ChatView;
import chat.network.TimeoutTimer;
import chat.objects.Client;

public class TimeoutTimerTest {
	private HashMap<Integer, Client> clients = new HashMap<Integer, Client>();
	private Client client0;
	private Client client1;
	private Client client2;

	private long startTime;

	public static void main(String[] args) {
		new TimeoutTimerTest();
	}

	public TimeoutTimerTest() {
		client0 = new Client(0, 0);
		client1 = new Client(1, 0);
		client2 = new Client(2, 0);
		setup();
		runTest1();
		runTest2();
		runTest3();
	}

	public void setup() {
		startTime = System.currentTimeMillis();

		AppView appView = new AppView(null);
		ChatView chatView = appView.addChatWindow("Testtab", null);

		clients = createClients();

		TimeoutTimer timeoutTimer = new TimeoutTimer(clients, chatView);
		timeoutTimer.start();
	}

	public void runTest1() {
		System.out.println("Starting timer for test 1.");
		try {
			Thread.sleep(4900);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(clients, createClients());

		System.out.println("Completed test 1.");
	}

	public void runTest2() {
		System.out.println("Starting timer for test 2.");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		HashMap<Integer, Client> testClients = new HashMap<Integer, Client>();
		testClients.put(2, client2);
		assertEquals(clients, testClients);

		System.out.println("Completed test 2.");
	}

	public void runTest3() {
		System.out.println("Starting timer for test 3.");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		HashMap<Integer, Client> testClients = new HashMap<Integer, Client>();
		assertEquals(clients, testClients);

		System.out.println("Completed test 3.");
	}

	public boolean assertEquals(Object expected, Object actual) {
		if (!actual.equals(expected)) {
			System.out.println("Expected: " + expected + ". Actual: " + actual);
			return false;
		}
		return true;
	}

	public HashMap<Integer, Client> createClients() {
		HashMap<Integer, Client> testClients = new HashMap<Integer, Client>();
		client0.setLastBro(startTime);
		client1.setLastBro(startTime);
		client2.setLastBro(startTime + 1000);
		testClients.put(0, client0);
		testClients.put(1, client1);
		testClients.put(2, client2);
		return testClients;
	}
}
