package kk;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public static String ipAddress;
	public static int portNumber = 8000;
	private JLabel ip, port, info1, info2;
	private JTextField newIp, newPort;
	private JButton newMessage;
	
	public MainFrame() {
		super("Communicator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setSize(250, 140);
		setVisible(true);
		setResizable(false);
		
		try{
			ipAddress = InetAddress.getLocalHost().getHostAddress();
			ip = new JLabel("Your IP: " + ipAddress);
		} catch(UnknownHostException uhe) {
			System.out.println("Didn't find Local Host.");
			ip = new JLabel("Your IP: Unknown");
		} finally {}
		
		// Looking for not taken port
		// We assume there's at least one
		boolean flag = true;
		while(flag) {
			flag = false;
			
			try{
				PortListener.ds = new DatagramSocket(portNumber);
			} catch(SocketException se) {
				flag = true;
				portNumber++;
			} finally {}
		}

		port = new JLabel("Your Port: " + Integer.toString(PortListener.ds.getLocalPort()));
		
		add(ip);
		add(port);
		
		info1 = new JLabel("    Type IP: ");
		add(info1);
		
		newIp = new JTextField(15);
		newIp.setToolTipText("Type Message Receiver's IP Address");
		newPort = new JTextField(15);
		newPort.setToolTipText("Type Message Receiver's Port Number");
		add(newIp);
		
		info2 = new JLabel("Type Port: ");
		add(info2);
		
		add(newPort);
		
		newMessage = new JButton("New Message");
		newMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChatFrame Chat = new ChatFrame(newIp.getText(), newPort.getText());
				
				NewMessagesArray.map.put(newIp.getText() + ":" + newPort.getText(), NewMessagesArray.nextNumber);
				NewMessagesArray.newMessage[NewMessagesArray.nextNumber] = "";
				NewMessagesArray.nextNumber++;
				MessageUpdater.newMessagesArray.addObserver(Chat);
			}
		});
		
		add(newMessage);
	}
}
