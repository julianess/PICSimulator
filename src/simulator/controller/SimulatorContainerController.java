package simulator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class SimulatorContainerController {
	
	@FXML
	public TextArea editor;
	
	@FXML
	public Button test_button;
	
	public void setTextField(String string){
		editor.appendText(string);
	}
	
	@FXML
	public void openFileTest(){
		setTextField("scheiße");
	}
}
