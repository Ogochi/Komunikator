package kk;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MessageSender {
	public static boolean send(String ip, String port, String message){
		DatagramSocket ds = null;
		
		MainController.exec.shutdown();
		PortListener.ds.close();
		
		try{
			ds = new DatagramSocket(MainFrame.portNumber);
		} catch(SocketException se) {
			System.out.println("Couldn't find any socket");
		} finally {
			byte[] b = message.getBytes();
			DatagramPacket packet = null;
			
			try{
				packet = new DatagramPacket(
					b, b.length, InetAddress.getByName(ip), Integer.parseInt(port));
			} catch(UnknownHostException uhe) {
				System.out.println("Invalid or not found IP address");
				MainController.listen();
				
				return false;
			} finally {}
			
			if(ds != null) {
				if(packet != null) {
					try {
						ds.send(packet);
					} catch(IOException ioe) {
						System.out.println("Couldn't send packet");
					} finally {}
				}
					
				ds.close();
			}
		}
		
		try {
			PortListener.ds = new DatagramSocket(MainFrame.portNumber);
		} catch (SocketException e) {}
		
		MainController.listen();
		
		return true;
	}
}
