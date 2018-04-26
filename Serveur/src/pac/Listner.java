package pac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Listner implements Runnable{
	private Socket socket;
	private HashMap<String, PrintWriter> outs;
	private HashMap<String, Integer> connectedUser;
	private BufferedReader in;
	private PrintWriter out;
	private UserBase UB;
	private String nam; 
	
	public Listner(Socket s, HashMap<String, PrintWriter> o, HashMap<String, Integer> c, UserBase b) {
		socket = s;
		outs = o;
		UB = b;
		connectedUser = c;
	}
	
	@Override
	public void run() {
		String msg;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			msg = in.readLine();
			System.out.println(msg);
			System.out.println(UB.getListe());
			String[]s = msg.split(":");
			if(!UB.exist(s[1], s[2])) {
				throw new Exception("connextion from :"+socket.getInetAddress()+" has field!!");
			}
			nam = s[1];
			outs.put(nam, out);
			connectedUser.put(nam, UB.getLevel(nam));
			//sendClientId();
			for (String key : outs.keySet()) {
				msg = ClientsId(key);
				outs.get(key).write(msg+"\n");
				outs.get(key).flush();
			}
			msg = in.readLine();
    		System.out.println("---"+msg);
            while(msg!=null){
            	if(msg.substring(0, 3).equals("DCO")) {
            		outs.remove(nam);
            		connectedUser.remove(nam);
            		for (String key : outs.keySet()) {
        				msg = ClientsId(key);
        				outs.get(key).write(msg+"\n");
        				outs.get(key).flush();
        			}
            	}else if(msg.substring(0, 3).equals("MSG")) {
            		s = msg.split(":");
        			String nmsg = s[0]+":"+s[1]+":"+nam+">> "+s[2];
            		if(msg.substring(4, 7).equals("bro")) {
            			for (String key : outs.keySet()) {
            				if(!this.out.equals(outs.get(key)) && connectedUser.get(key) <= connectedUser.get(nam)) {
            					outs.get(key).write(nmsg+"\n");
        						outs.get(key).flush();
            				}
    					}
            		}else {
            			outs.get(s[1]).write(nmsg+"\n");
            			outs.get(s[1]).flush();
            		}
            	}else throw new Exception("Invalid message");
            	
                msg =in.readLine();
            }
            System.out.println("client déconecté");
		} catch (Exception e) {
				System.out.println(e.getMessage());
				if(!socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
		}
		
	}
	
	public String capsule(String nam,String message){
		String[] m = message.split(":");
		m[2] = nam+" - "+m[2];
		return m[0]+":"+m[1]+":"+m[2];
	}
	
	public String normalize(int i) {
		if(i < 10) {
			return "00"+i;
		}else if(i < 100) {
			return "0"+i;
		}return ""+i;
	}
	
	/*public void sendClientId() {
		String msg = "CID:"+normalize(id);
		System.out.println("---CID start---");
		out.write(msg+"\n");
		out.flush();
		System.out.println("---CID end---");
	}*/
	
	public String ClientsId(String key) {
		String msg = "CON";
		for (String i : outs.keySet()) {
			if(connectedUser.get(i) <= connectedUser.get(key))
				msg = msg+":"+i;
		}
		return msg;
	}
}
