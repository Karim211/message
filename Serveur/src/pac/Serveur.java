package pac;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Serveur implements Runnable{
	private int port;
	private ServerSocket server;
	private HashMap<String, PrintWriter> outs;
	private HashMap<String, Integer> connectedUser;
	private Socket s;
	private UserBase UB;
	public Serveur(int port, String file) {
		this.port = port;
		UB = new UserBase(file);
	}
	@Override
	public void run() {
		
		try {
			outs=new HashMap<String, PrintWriter>();
			connectedUser = new HashMap<String, Integer>();
			server = new ServerSocket(port);
			System.out.println("le serveur est a l'ecoute du port : "+server.getLocalPort()+"\n");
			while (true) {
				s = server.accept();
				Thread t1 = new Thread(new Listner(s, outs, connectedUser, UB));
				t1.start();
			}
		}catch (Exception e) {
				System.out.println(e.getMessage());
		}
					
	}
	
}
