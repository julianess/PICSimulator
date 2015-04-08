package simulator;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private BorderPane rootLayout;
	private AnchorPane singleLayout;
	
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("PIC Simulator");

        initRootLayout();

        //showSimulatorContainer();
        
	}
	
	public void clickFileOpen(){
		
	}
	
	public void initRootLayout(){
		try {
			// Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/SingleLayout.fxml"));
            singleLayout = (AnchorPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(singleLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showSimulatorContainer(){
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/SimulatorContainer.fxml"));
            AnchorPane container = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(container);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage(){
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
