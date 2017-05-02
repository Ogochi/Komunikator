package kk;

import javax.swing.SwingUtilities;

public class MessageUpdater {
	public static NewMessagesArray newMessagesArray = new NewMessagesArray();
	
	public void sendNewMessage(String ip, String port, String message) {
		if(!NewMessagesArray.map.containsKey(ip + ":" + port))
			createNewChat(ip, port);
		
		NewMessagesArray.newMessage[NewMessagesArray.map.get(ip + ":" + port)] = message;
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				newMessagesArray.setChangedTrue();
				newMessagesArray.notifyObservers();
			}
		});
	}
	
	private void createNewChat(String ip, String port) {
		NewMessagesArray.map.put(ip + ":" + port, NewMessagesArray.nextNumber);
		NewMessagesArray.newMessage[NewMessagesArray.nextNumber] = "";
		NewMessagesArray.nextNumber++;
	
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ChatFrame chat = new ChatFrame(ip, port);
				newMessagesArray.addObserver(chat);
			}
		});	
	}
}
