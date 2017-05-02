package kk;

import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatFrame extends JFrame implements Observer {
	private static final long serialVersionUID = 2L;
	
	private String ip, port;
	
	private JTextArea chatArea, newMessage;
	private JLabel info;
	private JScrollPane scroll;
	
	private enum Person{
		NOBODY, YOU, FRIEND;
	}
	
	private Person Lastmessage = Person.NOBODY;
	
	public ChatFrame(String ip, String port) {
		super(MainFrame.ipAddress + " : " + MainFrame.portNumber + " chats with: " + ip + " : " + port);
		this.ip = ip;
		this.port = port;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new FlowLayout());
		setSize(500, 500);
		setVisible(true);
		setResizable(false);
		
		chatArea = new JTextArea(25, 40);
		newMessage = new JTextArea(1, 40);
		
		scroll = new JScrollPane(chatArea);

		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatArea.setToolTipText("Press 'ENTER' to send message");
		
		newMessage.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					
					sendMessage(newMessage.getText());
					newMessage.setText(null);
				}
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
		
		info = new JLabel("Your Message:");
		
		add(scroll);
		add(info);
		add(newMessage);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				NewMessagesArray.map.remove(ip + ":" + port);
			}
		});
	}
	
	@Override
	public void update(Observable newMessagesArray, Object nullarg) {
		String message = NewMessagesArray.newMessage[NewMessagesArray.map.get(ip + ":" + port)];

		if(!message.equals(""))
			receiveMessage(message);
		
		NewMessagesArray.newMessage[NewMessagesArray.map.get(ip + ":" + port)] = "";
	}
	
	public synchronized void receiveMessage(String message) {
		if(Lastmessage != Person.FRIEND) {
			chatArea.append("\t----FRIEND----\n");
			Lastmessage = Person.FRIEND;
		}
		
		if(!message.equals("") && !message.equals("\n")) {
			chatArea.append(message + "\n");
			
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	public synchronized void sendMessage(String message) {
		if(!message.equals("")) {
			if(MessageSender.send(ip, port, message) == false) {
				try{
					TimeUnit.SECONDS.sleep(5);
				} catch(InterruptedException ie) {
					System.out.println("Sleep was interrupted");
				} finally {
					setVisible(false);
					dispose();
				}
			}
			
			if(Lastmessage != Person.YOU) {
				chatArea.append("\t----YOU----\n");
				Lastmessage = Person.YOU;
			}
				
			chatArea.append(message + "\n");
		}
	}
}