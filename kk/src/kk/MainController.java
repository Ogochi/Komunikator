package kk;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

public class MainController {
	private static MainFrame main;
	
	public static ExecutorService exec;
	
	public static MessageUpdater messageupdater = new MessageUpdater();
	
	public static void main(String[] args) throws Exception {
		//System.out.println( exec.submit(new IsIPReachable("192.168.56.6")).get() );
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				 main = new MainFrame();
				 
				 main.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e)
					{
						exec.shutdown();
						PortListener.ds.close();
					}
				 });
			}
		});
		
		TimeUnit.SECONDS.sleep(1);
		listen();
	}
	
	public static void listen() {
		exec = Executors.newFixedThreadPool(1, new DaemonThreadFactory());
		
		exec.execute(new PortListener());
	}
}
