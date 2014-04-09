package chat.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class View extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 792191282620003872L;
	JTextArea receive = new JTextArea();
	JTextArea text = new JTextArea();
	JButton send = new JButton("send");
	JButton clear = new JButton("clear");

	public static void main(String[] args) {
		View gui = new View();
	}
	
	public View(){
		super("View");
		buildGUI();
		setVisible(true);
	}
	
	public void buildGUI(){
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(600, 400);
		JPanel buttons = new JPanel();
		receive.setEditable(false);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu jMenu = new JMenu("File");
		menuBar.add(jMenu);
		JMenuItem menuItem = new JMenuItem("Dit is een item");
		jMenu.add(menuItem);
		
		buttons.add(clear);
		clear.addActionListener(this);
		buttons.add(send);	
		send.addActionListener(this);
		Container cc = getContentPane();
		cc.setLayout(new GridLayout(3,1,10,10));
		cc.add(receive);
		cc.add(text);
		cc.add(buttons);
	}
	
	public void actionPerformed(ActionEvent ev) {	
		Object src = ev.getSource();
		if (src == clear) {
			text.setText(null);			
		}
		if (src == send) {
			text.setText(null);
		}
	}
}
