package kk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class PortListener implements Runnable {
	public static DatagramSocket ds;
	@Override
	public void run() {
		DatagramPacket dp;
		
		boolean isActive = true;

		while(!ds.isClosed()) {
			byte[] receivedMessage = new byte[1000];
			dp = new DatagramPacket(receivedMessage, receivedMessage.length);

			try {
				ds.receive(dp);
			} catch (IOException ioe) {
				//System.out.println("Error during receiving message in Port Listener");
				//System.out.println("Socket probably was closed");
				isActive = false;
			} finally {}
			
			if(isActive) {
				MainController.messageupdater.sendNewMessage(dp.getAddress().getHostAddress(),
															Integer.toString(dp.getPort()),
															new String(dp.getData()));
			}
		}
	}
}
