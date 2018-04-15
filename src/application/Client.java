package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
	Socket socket;
	BufferedReader in;
	@Override
	public void run() {
		
		try {
			System.out.println("Demande de connection");
			socket = new Socket("127.0.0.1" ,3200);
			System.out.println("Demande de connection");
			
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}
	public void send(String message) {
		
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			// futur id + message = message finale
			out.write(message);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(message);
	}
	
	public String receive() throws Exception {
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String message = in.readLine();
		return message;
	}
}
