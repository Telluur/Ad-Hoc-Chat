package chat.gui;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import chat.network.NetworkController;

public class View extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 792191282620003872L;
	private JTextArea receive = new JTextArea();
	private JTextField text = new JTextField();
	private JScrollPane scrollText = new JScrollPane(receive);
	private JButton send = new JButton("send");
	private JButton clear = new JButton("clear");

	public static final String lineSeparator = System.getProperty("line.separator");

	private NetworkController networkController;

	public View(NetworkController networkController) {
		super("View");
		buildGUI();
		setVisible(true);
		this.networkController = networkController;
	}

	public JTextArea getReceive() {
		return receive;
	}

	public void addText(int source, String message) {
		addText(source + ": " + message);
	}

	public void addText(String message) {
		final JScrollBar scrollBar = scrollText.getVerticalScrollBar();
		boolean end = scrollBar.getMaximum() == scrollBar.getValue() + scrollBar.getVisibleAmount();
		System.out.println(end);

		receive.append(message + lineSeparator);

		if (end) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							scrollBar.setValue(scrollBar.getMaximum());
						}
					});
				}
			});
		}

	}

	public void buildGUI() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(600, 400);

		text.addActionListener(this);

		receive.setEditable(false);
		receive.setLineWrap(true);
		receive.setWrapStyleWord(true);
		scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// Add menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu jMenu = new JMenu("File");
		menuBar.add(jMenu);
		JMenuItem menuItem = new JMenuItem("Dit is een item");
		jMenu.add(menuItem);

		// Add buttons
		JPanel buttons = new JPanel();
		buttons.add(clear);
		clear.addActionListener(this);
		buttons.add(send);
		send.addActionListener(this);

		// Add container
		Container cc = getContentPane();
		cc.setLayout(new GridLayout(3, 1, 10, 10));
		cc.add(scrollText);
		cc.add(text);
		cc.add(buttons);
	}

	public void actionPerformed(ActionEvent ev) {
		System.out.println("Action event!");
		Object src = ev.getSource();
		if (src == clear) {
			text.setText(null);
			receive.setText(null);
		} else if (src == send || src == text) {
			if (!text.getText().isEmpty()) {
				System.out.println(scrollText.getVerticalScrollBar().getValue() + " , " + scrollText.getVerticalScrollBar().getAlignmentY());
				networkController.send(text.getText());
				text.setText(null);
			}
		}
	}
}
