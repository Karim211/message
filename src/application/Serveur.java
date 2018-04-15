package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

public class Serveur implements Runnable{
	private int port;
	ServerSocket serveur;
	HashMap<Integer, Socket>socketServeur=new HashMap<Integer, Socket>();
	PrintWriter out;
	BufferedReader in;
	
	public Serveur(int port) {
		this.port = port;
	}
	@Override
	public void run() {
		
		try {
			serveur = new ServerSocket(port);
			System.out.println("le serveur est a l'ecoute du port : "+serveur.getLocalPort());
			Socket s = serveur.accept();
			socketServeur.put(1,s);
			//envoie id_1
			socketServeur.put(2,serveur.accept());
			Thread t = new Thread(new Runnable(){ 
				
				@Override
				public void run() {
					try {
						while(true) {
							in = new BufferedReader(new InputStreamReader(socketServeur.get(1).getInputStream()));
							String message = in.readLine();
							System.out.println(message);
							out = new PrintWriter(socketServeur.get(2).getOutputStream());
							//---------------------------------------------------------------
							out.write(message);
							out.flush();
						}
						
					}catch (Exception e) {
						e.printStackTrace();
					}	
				}
			});
			t.start();
			while(true) {
				in = new BufferedReader(new InputStreamReader(socketServeur.get(2).getInputStream()));
				String message = in.readLine();
				System.out.println(message);
				out = new PrintWriter(socketServeur.get(1).getOutputStream());
				out.write(message);
				out.flush();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close() {
		try {
			//socketServeur.close();
			serveur.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
