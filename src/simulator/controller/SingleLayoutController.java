package simulator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;

public class SingleLayoutController {
	
	@FXML
	private MenuItem openFile;
	
	@FXML
	private MenuItem closeFile;
	
	@FXML
	private TextArea editor;
	
	@FXML
	public void onClickOpenFile(){
		editor.setText("funktioniert!");
		
	}

}
