package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements Runnable{

	@Override
	public void run() {
		Socket socket;
		BufferedReader in;
		try {
			System.out.println("Demande de connection");
			socket = new Socket("192.168.43.71" ,3500);
			System.out.println("Demande de connection");
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = in.readLine();
			System.out.println(message);
			socket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

}
