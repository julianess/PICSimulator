package simulator.controller;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SingleLayoutController {

	@FXML
	private MenuItem openFile;

	@FXML
	private MenuItem closeFile;

	@FXML
	private TextArea editor;

	@FXML
	public void onClickOpenFile() {

		//Variablen
		
		//Inhaltsstring fuer das Textfenster
		String Inhalt = "";
		
		//Charset fuer Umlaute
		Charset charset = Charset.forName("ISO-8859-1");
		
		FileChooser fileChooser = new FileChooser();

		Stage primaryStage = (Stage) editor.getScene().getWindow();

		fileChooser.setTitle("Choose a File");
		Path file = Paths.get(fileChooser.showOpenDialog(primaryStage).getPath());

		try {
			List<String> lines = Files.readAllLines(file, charset);
			for (String line : lines)
			{
				//Fuegt nach jeder eingelesenen Zeile einen Zeilenumbruch ein
				Inhalt += line + "\n";
			}
			// Entfernt den Zeilenumbruch nach der letzten Zeile
			Inhalt = Inhalt.substring(0, Inhalt.length() - 1); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		editor.setText(Inhalt);
	}
}
