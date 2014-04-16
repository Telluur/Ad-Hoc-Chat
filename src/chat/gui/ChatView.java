package chat.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import chat.objects.Chat;

public class ChatView extends JPanel implements ActionListener {

	private static final long serialVersionUID = 792191282620003872L;
	public static final String lineSeparator = System.getProperty("line.separator");
	private Chat chat;

	// Upper grid of layout; shows all messages.
	private JTextArea receive = new JTextArea();
	private JScrollPane scrollText = new JScrollPane(receive);

	// lower grid of layout; input field and buttons.
	private JTextField text = new JTextField();
	private JButton send = new JButton("send");
	private JButton clear = new JButton("clear");

	public ChatView(Chat chat) {
		this.chat = chat;
		
		
		// Add buttons
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2, 1));
		buttons.add(clear);
		clear.addActionListener(this);
		buttons.add(send);
		send.addActionListener(this);
		text.addActionListener(this);

		// Upper grid of layout.
		scrollText.setPreferredSize(new Dimension(AppView.WIDTH, AppView.HEIGHT - 50));

		this.add(scrollText);

		// Lower grid of layout.
		JPanel inputContainer = new JPanel();
		inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.X_AXIS));
		inputContainer.add(text);
		inputContainer.add(buttons);
		inputContainer.setPreferredSize(new Dimension(AppView.WIDTH, 50));
		this.add(inputContainer);

		// receive.addActionListener(this);

		receive.setEditable(false);
		receive.setLineWrap(true);
		receive.setWrapStyleWord(true);
		scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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

		// Queue event to force scrollbar re-calculation, Swing & AWT are lazy
		// bastards.
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

	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		System.out.println("Enter clicked!");
		if (src == clear) {
			text.setText(null);
			receive.setText(null);
		} else if (src == send || src == text) {
			if (!text.getText().isEmpty()) {
				System.out.println(scrollText.getVerticalScrollBar().getValue() + " , " + scrollText.getVerticalScrollBar().getAlignmentY());
				chat.send(text.getText());
				text.setText(null);
			}
		}
	}

}
