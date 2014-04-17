package chat.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import chat.network.NetworkController;
import chat.objects.Chat;

public class AppView extends JFrame implements ActionListener {

    private static final long serialVersionUID = -2915063676407599060L;
    private JTabbedPane tabbedPane;
    private JMenu jMenu;

    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;
    
    private NetworkController networkController;

    public AppView(NetworkController networkController) {
        super("Chat Application");
        this.networkController = networkController;
        buildGUI();
        this.setVisible(true);
    }

    private void buildGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH + 10, HEIGHT + 60);
        setResizable(false);

        // Create menubar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        jMenu = new JMenu("Private Chat");
        menuBar.add(jMenu);

        // create Tabbedpane
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // Create layout
        add(tabbedPane);
    }

    public void addClient(String clientName) {
        JMenuItem menuItem = new JMenuItem(clientName);
        jMenu.add(menuItem);
        menuItem.addActionListener(this);
    }

    public void removeClient(String clientName) {
        for (int i = 0; i < jMenu.getItemCount(); i++) {
            if (jMenu.getItem(i).getText().equals(clientName)) {
                jMenu.remove(i);
            }
        }
    }

    public ChatView addChatWindow(String tabName, Chat chat) {
        ChatView tabContent = new ChatView(chat);
        tabbedPane.addTab(tabName, tabContent);
        return tabContent;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if(source instanceof JMenuItem){
        	JMenuItem menuItem = (JMenuItem) source;
        	networkController.startPrivateChat(Integer.parseInt(menuItem.getText()));
        }
    }
}
