package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;

public class MainController {
	@FXML private Button send;
	@FXML private Button connect;
	@FXML private RadioButton broad;
	@FXML private RadioButton sing;
	@FXML private RadioButton multi;
	@FXML private TextArea idA;
	@FXML private TextArea text;
	@FXML private ChoiceBox<String> idM;
	@FXML private ChoiceBox<String> idS;
	@FXML private TextField message;
	@FXML private TextField ip;
	@FXML private Label idU;
	
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	int res;
	static ArrayList<String> idClient;
	ArrayList<String> listRes;
	static String nam;
	Thread t;
	
	@FXML private void initialize() {
		
		text.setEditable(false);
		idA.setEditable(false);
		idM.setDisable(true);
		idS.setDisable(true);
		message.setDisable(true);
		idClient = new  ArrayList<String>();
		listRes = new  ArrayList<String>();
		
		send.setOnAction(e -> {
			if(!message.getText().equals("") && socket != null) {
				
				try {
					if(broad.isSelected()) {
						System.out.println(capsule("MSG", "bro", message.getText()));
						out.write(capsule("MSG", "bro",message.getText())+"\n");
						out.flush();
					}else if(sing.isSelected()) {
						if(idS.getSelectionModel().getSelectedItem() == null) throw new Exception("Please enter a receiver");
						System.out.println(capsule("MSG", idS.getSelectionModel().getSelectedItem(), message.getText()));
						out.write(capsule("MSG", idS.getSelectionModel().getSelectedItem(),message.getText())+"\n");
						out.flush();
					}else {
						if(listRes.isEmpty())throw new Exception("No receiver selected");
						for (String i : listRes) {
							System.out.println(capsule("MSG", i, message.getText()));
							out.write(capsule("MSG", i,message.getText())+"\n");
							out.flush();
						}
					}
					text.appendText(message.getText()+"\n");
				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Error");
					alert.setHeaderText(e1.getMessage());

					alert.show();
				}
				
				message.clear();
			}
		});
		message.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER && !message.getText().equals("") && socket != null) {

				try {
					if(broad.isSelected()) {
						System.out.println(capsule("MSG", "bro", message.getText()));
						out.write(capsule("MSG", "bro",message.getText())+"\n");
						out.flush();
					}else if(sing.isSelected()) {
						if(idS.getSelectionModel().getSelectedItem() == null) throw new Exception("Please enter a receiver");
						System.out.println(capsule("MSG", idS.getSelectionModel().getSelectedItem(), message.getText()));
						out.write(capsule("MSG", idS.getSelectionModel().getSelectedItem(),message.getText())+"\n");
						out.flush();
					}else {
						if(listRes.isEmpty())throw new Exception("No receiver selected");
						for (String i : listRes) {
							System.out.println(capsule("MSG", i, message.getText()));
							out.write(capsule("MSG", i,message.getText())+"\n");
							out.flush();
						}
					}
					text.appendText(message.getText()+"\n");
				} catch (Exception e1) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Error");
					alert.setHeaderText(e1.getMessage());

					alert.show();
				}
				
				message.clear();
			}
		});
		
		sing.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					idS.setDisable(false);
					initList();
				}else {
					idS.setDisable(true);
				}
				
			}
		});
		
		multi.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					idM.setDisable(false);
					idA.setDisable(false);
					System.out.println("+++"+idClient);
					initList();
				}else {
					idM.setDisable(true);
					idA.clear();
					idA.setDisable(true);
					listRes.clear();
				}
				
			}
		});
		
		idM.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if((int)newValue != -1) {
					listRes.add(idM.getItems().get((int)newValue));
					idA.appendText(idM.getItems().get((int)newValue)+"\n");
				}
			}
		});
		
		connect.setOnAction(e -> {
			if(socket != null) {
				 out.write("DCO");
				 out.flush();
			}
			try {
				
				if(t != null) {
					socket.close();
					t = null;
				}
				TextInputDialog dialog = new TextInputDialog();
				dialog.setContentText("user name");
				dialog.setHeaderText("");
				dialog.setTitle("Connect");
				Optional<String> userN = dialog.showAndWait();
				PasswordDialog pass = new PasswordDialog();
				Optional<String> userP = pass.showAndWait();
				
				System.out.println("Demande de connection");
				socket = new Socket(ip.getText() ,50020);
				out = new PrintWriter(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				nam = userN.get();
				out.write("PAS:"+nam+":"+userP.get()+"\n");
				out.flush();
				Thread.sleep(2000);

				t = new Thread(new Listner(in, text));
				t.start();
				message.setDisable(false);
				idU.setText(nam);
			} catch (Exception ex) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Error");
				if(ex.getMessage().equals("Socket closed")) {
					alert.setHeaderText("Connection denied");
				}else alert.setHeaderText(ex.getMessage());
				alert.show();
			}
			
		});
		
		System.out.println("---------");
	}
	
	public String capsule(String protocole,String reciver,String message){
		return protocole+":"+reciver+":"+message;
	}
	
	public void initList() {
		idS.getItems().clear();
		idM.getItems().clear();
		idS.getItems().addAll(idClient);
		idM.getItems().addAll(idClient);
	}
	
	public String normalize(int i) {
		if(i < 10) {
			return "00"+i;
		}else if(i < 100) {
			return "0"+i;
		}return ""+i;
	}

	public void exit() {
		System.out.println("déconnection");
		out.write("DCO");
		out.flush();
		t.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText(e.getMessage());

			alert.show();
		}
	}
	
	
	
}
