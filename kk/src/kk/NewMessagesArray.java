package kk;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class NewMessagesArray extends Observable {
	public static String[] newMessage = new String[1000];
	public static int nextNumber = 0;
	public static Map<String, Integer> map = new HashMap<String, Integer>();
	
	public void setChangedTrue() {
		setChanged();
	}
}
