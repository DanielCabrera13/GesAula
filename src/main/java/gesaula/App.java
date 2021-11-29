package gesaula;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	
	public static Stage primaryStage;
	
	private MainController mainController;

	@Override
	public void start(Stage primaryStage) throws Exception {

		
		mainController = new MainController();
		
		Scene scene = new  Scene(mainController.getView());
		
		primaryStage.setTitle("GesAula");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	}
	
	public static void main(String[] args) {
		launch(args);

	}

}
