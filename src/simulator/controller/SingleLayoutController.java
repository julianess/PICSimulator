package simulator.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFileChooser;

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
		//String file = "C:\\Users\\Dennis\\Desktop\\Test.txt";
		String Inhalt = "";
		String File;
		
		JFileChooser chooser = new JFileChooser();
		int rueckgabeWert = chooser.showOpenDialog(null);
        
        /* Abfrage, ob auf "Öffnen" geklickt wurde */
        if(rueckgabeWert == JFileChooser.APPROVE_OPTION)
        {
             // Ausgabe der ausgewaehlten Datei
             File = chooser.getSelectedFile().getPath();
           try
           {
                 List<String> lines = Files.readAllLines(Paths.get(File), Charset.defaultCharset());
                 for (String line : lines)
                 {
                     Inhalt += line + "\n"; //Fügt nach jeder eingelesenen Zeile einen Zeilenumbruch ein
                 }
                 Inhalt = Inhalt.substring(0, Inhalt.length()-1); //Entfernt den Zeilenumbruch nach der letzten Zeile
            } 
     		catch (IOException e)
     		{
                 e.printStackTrace();
             }
     		
     		editor.setText(Inhalt);
        }
	}
}
