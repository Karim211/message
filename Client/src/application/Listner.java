package application;

import java.io.BufferedReader;
import java.util.ArrayList;

import javafx.scene.control.TextArea;

public class Listner implements Runnable{
	private BufferedReader in;
	private TextArea text;
	public Listner(BufferedReader b, TextArea t) {
		in = b;
		text = t;
	}
	@Override
	public void run() {
		String msg;
		try {			
			msg =in.readLine();
			System.out.println(msg);

            while(msg!=null){
            	if(msg.substring(0, 3).equals("MSG")) {
            		System.out.println("-----"+msg.substring(8));
                    text.appendText("\t\t"+msg.substring(8)+"\n");
            	}else if(msg.substring(0, 3).equals("CON")) {
            		MainController.idClient.clear();
            		MainController.idClient = getIds(msg);
            		System.out.println(MainController.idClient);
            	}/*else if(msg.substring(0, 3).equals("CID")) {
            		String []s = msg.split(":");
            		nam = s[1];
            	}*/else throw new Exception("invalide message");
            	
                msg =in.readLine();

            }
            System.out.println("Serveur déconecté");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	private ArrayList<String> getIds(String id2) {
		ArrayList<String> ids = new ArrayList<String>();
		if(id2.length() > 3) {
			id2 = id2.substring(4);
			String[] s = id2.split(":");
			for (String string : s) {
				if(!string.equals(MainController.nam)) {
					ids.add(string);
				}
			}
		}
		return ids;
	}
	
}
