package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

public class MainController {
	@FXML private Button send;
	@FXML private Button connect;
	@FXML private TextField message;
	@FXML private TextArea text;
	@FXML private TextField textId;
	@FXML private TextField textRec;
	Socket socket;
	ObjectOutputStream out;
	ObjectInputStream in;
	String id;
	String rec;
	Thread listener;
	
	@FXML private void initialize() {
		try {
			System.out.println("Demande de connection");
			socket = new Socket("127.0.0.1" ,3200);
			System.out.println("Demande accepte");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		send.setOnAction(e -> {
			if(message.getText()!="") {
				text.appendText(message.getText()+"\n");
				try {
					System.out.println(capsule("MES", message.getText()));
					out.writeObject(capsule("MES", message.getText()));
					out.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				message.clear();
			}
		});
		message.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER && message.getText()!="") {
				text.appendText(message.getText()+"\n");
				try {
					out.writeObject(capsule("MES", message.getText()));
					out.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				message.clear();
			}
		});
		connect.setOnAction(e -> {
			id = textId.getText().trim();
			rec = textRec.getText().trim();
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						out = new ObjectOutputStream(socket.getOutputStream());
						out.writeObject(capsule("CON", ""));
						out.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			t.start();
			
			text.clear();
			//si le client est deja connecter 
			if(listener != null)
				listener.interrupt();
			listener = new Thread(new Runnable() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					
					ArrayList<String> msg;
					try {
						in = new ObjectInputStream(socket.getInputStream());
						msg =(ArrayList<String>) in.readObject();
		                while(msg!=null){
		                	System.out.println("-----"+msg.get(3));
		                    text.appendText("\t\t"+msg.get(3)+"\n");
		                    msg = (ArrayList<String>) in.readObject();
		                }
		                System.out.println("Serveur déconecté");
		                out.close();
					} catch (Exception e) {
							
							e.printStackTrace();
					}
					
				}
			});
			listener.start();
		});

		System.out.println("---------");
	}
	public ArrayList<String> capsule(String protocole,String message){
		ArrayList<String> m = new ArrayList<String>();
		m.add(protocole);m.add(id);m.add(rec);m.add(message);
		return m;
	}
	
	
}
