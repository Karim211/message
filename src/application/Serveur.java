package application;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur implements Runnable{
	private int port;
	ServerSocket serveur;
	Socket socketServeur;
	PrintWriter out;
	public Serveur(int port) {
		this.port = port;
	}
	@Override
	public void run() {
		
		try {
			serveur = new ServerSocket(port);
			System.out.println("le serveur est a l'ecoute du port"+serveur.getLocalPort());
			socketServeur = serveur.accept();
			System.out.println("un client c'est connecte");
			out = new PrintWriter(socketServeur.getOutputStream());
			out.write("connect");
			out.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close() {
		try {
			socketServeur.close();
			serveur.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
