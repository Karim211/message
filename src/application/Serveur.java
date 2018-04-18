package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Serveur implements Runnable{
	private int port;
	ServerSocket serveur;
	HashMap<String, Socket>socketServeur;
	
	
	public Serveur(int port) {
		this.port = port;
	}
	@Override
	public void run() {
		
		try {
			socketServeur=new HashMap<String, Socket>();
			serveur = new ServerSocket(port);
			System.out.println("le serveur est a l'ecoute du port : "+serveur.getLocalPort()+"\n");
			Thread t0 = new Thread(new Runnable() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					while (true) {
						Socket s;
						ArrayList<String> id = new ArrayList<>();
						try {
							ObjectOutputStream out ;
							ObjectInputStream in;
							s = serveur.accept();
							in = new ObjectInputStream(s.getInputStream());
							id = (ArrayList<String>) in.readObject();
							// message [protocol][id sender][id receiver][message]
							// protocole MSG == message && CON == connection && CRY == cryptage
							if(id.get(1) != "" || !socketServeur.containsKey(id.get(1))) {
								socketServeur.put(id.get(1),s);
							}else if(id.get(1) != "" && socketServeur.containsKey(id.get(1))) 
								throw new Exception("id already exists");
							else throw new Exception("error id null");
							out = new ObjectOutputStream(socketServeur.get(id.get(2)).getOutputStream());
							Thread t1 = new Thread(new Runnable(){ 
								
								@SuppressWarnings("unchecked")
								@Override
								public void run() {
									try {
										ArrayList<String> message;
										message = (ArrayList<String>)in.readObject();
										while(message != null) {
											
											//---------------------------------------------------------------
											out.writeObject(message);
											out.flush();
											message =(ArrayList<String>) in.readObject();
										}s.close();
										
									}catch (Exception e) {
										e.printStackTrace();
									}	
								}
							});
							t1.start();
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							Alert a = new Alert(AlertType.ERROR);
							a.setHeaderText(e.getMessage());
							a.showAndWait();
						}
						
					}
					
				}
			});
			t0.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void close(int i) {
		try {
			socketServeur.get(i).close();
			//serveur.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void creatListener(Socket s) {
		Thread t1 = new Thread(new Runnable(){ 
			
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				try {
					ArrayList<String> message;
					
					ObjectInputStream in = new ObjectInputStream(s.getInputStream());
					message = (ArrayList<String>)in.readObject();
					String r = message.get(2);
					ObjectOutputStream out = new ObjectOutputStream(socketServeur.get(r).getOutputStream());
					while(message != null) {
						
						//---------------------------------------------------------------
						out.writeObject(message);
						out.flush();
						message =(ArrayList<String>) in.readObject();
					}s.close();
					
				}catch (Exception e) {
					e.printStackTrace();
				}	
			}
		});
		t1.start();
	}
	
}
