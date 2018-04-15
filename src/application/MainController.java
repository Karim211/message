package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {
	@FXML private Button send;
	@FXML private TextField message;
	@FXML private TextArea text;
	@FXML private void initialize() {
		Client cRun = new Client();
		Thread serveur = new Thread(cRun);
		serveur.start();
		send.setOnAction(e -> {
			text.appendText(message.getText()+"\n");
			cRun.send(message.getText()+"\n");
			message.clear();
			//cRun.close();
		});
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						System.out.println(cRun.receive());
						text.appendText(cRun.receive());
					} catch (Exception e) {
						
						e.printStackTrace();
					}
				}
				
			}
		});
		
	}
}
