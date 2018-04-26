package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("App.fxml"));
			AnchorPane page = loader.load();
            Scene scene = new Scene(page);
			scene.getStylesheets().add(getClass().getResource("material-fx-v0_3.css").toExternalForm());
			primaryStage.setScene(scene);
			MainController controller = loader.getController();
			primaryStage.setOnHidden(e -> controller.exit());
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
