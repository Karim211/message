package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {
	@FXML private Button send;
	@FXML private TextField message;
	@FXML private TextArea text;
	@FXML private void initialize() {
		Serveur sRun = new Serveur(3200);
		Thread serveur = new Thread(sRun);
		serveur.start();
		send.setOnAction(e -> {
			text.appendText(message.getText()+"\n");
			message.clear();
			System.out.println(sRun.serveur.getLocalPort());
			//sRun.close();
		});
		
	}
}
