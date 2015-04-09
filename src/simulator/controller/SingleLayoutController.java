package simulator.controller;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFileChooser;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SingleLayoutController {

	@FXML
	private MenuItem openFile;

	@FXML
	private MenuItem closeFile;

	@FXML
	private TextArea editor;

	private AnchorPane singleLayout;

	@FXML
	public void onClickOpenFile() {

		FileChooser fileChooser = new FileChooser();

		Stage primaryStage = (Stage) editor.getScene().getWindow();

		fileChooser.setTitle("Choose a File");
		Path file = Paths.get(fileChooser.showOpenDialog(primaryStage).getPath());
		String Inhalt = "";

		try {
			List<String> lines = Files.readAllLines(file);
			for (String line : lines) {
				Inhalt += line + "\n"; // F�gt nach jeder eingelesenen Zeile
										// einen Zeilenumbruch ein
			}
			// Entfernt den Zeilenumbruch nach der letzten Zeile
			Inhalt = Inhalt.substring(0, Inhalt.length() - 1); 
		} catch (IOException e) {
			e.printStackTrace();
		}

		editor.setText(Inhalt);
	}
}
