package simulator.controller;


import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
	private MenuItem saveFile;

	@FXML
	private TextArea editor;

	//File oeffnen
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
		
		//Textarea in Zeilen splitten und zeilenweise in das alleZeilen Array schreiben
				String[] alleZeilen = editor.getText().split("\n");
				//Testweise Ausgabe des Array
				for (int i = 0; i < alleZeilen.length;i++)
				{
					System.out.println(alleZeilen[i]);
				}
	}
	
	//File speichern
	@FXML
	public void onClickSaveFile() {

		FileChooser fileChooser = new FileChooser();
		Stage primaryStage = (Stage) editor.getScene().getWindow();
		
	    //String zeilenweise aufteilen
		String[] alleZeilen = editor.getText().split("\n");
		
		//Dateipfad setzen
	    Path file = Paths.get(fileChooser.showSaveDialog(primaryStage).getPath());

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toString())))
	    {
	    	//Zeilenweise in die Datei schreiben und nach jeder Zeile einen Zeilenumbruch einfuegen
	    	for(int i = 0; i < alleZeilen.length; i++)
	    	{
	    		writer.write(alleZeilen[i]);
	    		if(i < alleZeilen.length - 1)
	    		{
	    			writer.newLine();
	    		}
	    	}
	    	writer.close();
	    } catch (IOException x) {
	      System.err.println(x);
	    }
	  }
	
	//Dokumentation oeffnen
	//Doku.pdf muss im bin Ordner des Programms liegen
	@FXML
	public void onClickDoku() {
		//Dateipfad des Programms erhalten, %20 durch Leerzeichen ersetzen und \Doku.pdf anfuegen
		File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ") + "\\Doku.pdf");
		 
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
