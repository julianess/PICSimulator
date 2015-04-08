package simulator.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

public class RootWindowController {

	@FXML
	private MenuItem openFile;
	
	
	private SimulatorContainerController controller;
	
	@FXML
	public void openFile(){
		controller.editor.appendText("JA!!!");
	}
	
	@FXML
    private void initialize() {
    }
}
