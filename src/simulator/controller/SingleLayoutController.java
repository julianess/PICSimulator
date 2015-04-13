package simulator.controller;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import simulator.BefehlDecoder;
import simulator.ValueClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SingleLayoutController {

	private ObservableList<ValueClass> data = FXCollections.observableArrayList();
	private int programcounter = 0;
	
	@FXML
	private MenuItem openFile;
	
	//Tabelle fuer Code
	@FXML
	private TableView<ValueClass> table;
	
	//Spalten erzeugen
	TableColumn<ValueClass, String> table_pcl = new TableColumn<ValueClass, String>("PCL");
	TableColumn<ValueClass, String> table_code = new TableColumn<ValueClass, String>("Code");
	TableColumn<ValueClass, String> table_zusatz = new TableColumn<ValueClass, String>("Zusatz");
	
	//File oeffnen
	@FXML
	public void onClickOpenFile() {
		
		
		//Charset fuer Umlaute
		Charset charset = Charset.forName("ISO-8859-1");
		
		FileChooser fileChooser = new FileChooser();

		Stage primaryStage = (Stage) table.getScene().getWindow();

		fileChooser.setTitle("Choose a File");
		Path file = Paths.get(fileChooser.showOpenDialog(primaryStage).getPath());
		
		table_pcl.setCellValueFactory(new PropertyValueFactory<ValueClass, String>("text_pcl"));
		table_pcl.setEditable(false);
		table_pcl.setSortable(false);
		table_pcl.setMinWidth(40);
		table_pcl.setMaxWidth(40);
		table_code.setCellValueFactory(new PropertyValueFactory<ValueClass, String>("text_code"));
		table_code.setEditable(false);
		table_code.setSortable(false);
		table_code.setMinWidth(40);
		table_code.setMaxWidth(40);
		table_zusatz.setCellValueFactory(new PropertyValueFactory<ValueClass, String>("text_zusatz"));
		table_zusatz.setEditable(false);
		table_zusatz.setSortable(false);

		try {
			List<String> lines = Files.readAllLines(file, charset);
			for (String line : lines)
			{	
				data.add(new ValueClass(line.substring(0, 4).trim(), line.substring(5, 9).trim(), line.substring(10, line.length()).trim()));
			}
			// Entfernt den Zeilenumbruch nach der letzten Zeile
			//Inhalt = Inhalt.substring(0, Inhalt.length() - 1); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		table.setItems(data);
		table.getColumns().add(table_pcl);
		table.getColumns().add(table_code);
		table.getColumns().add(table_zusatz);
	}
	
	//Dokumentation oeffnen
	//Doku.pdf muss im bin Ordner des Programms liegen
	@FXML
	public void onClickDoku() {
		//Dateipfad des Programms erhalten, %20 durch Leerzeichen ersetzen und \Doku.pdf anfuegen
		File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ") + "/Doku.pdf");
		 
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public short run()
	{
		String pclString;
		if(programcounter < 16)
			pclString = "000" + Integer.toHexString(programcounter).toUpperCase();
		else if(programcounter < 256)
			pclString = "00" + Integer.toHexString(programcounter).toUpperCase();
		else if(programcounter < 4096)
			pclString = "0" + Integer.toHexString(programcounter).toUpperCase();
		else
			pclString = Integer.toHexString(programcounter).toUpperCase();
		for(int i = 1; i <= table.getItems().size() ;i++)
		{
			if(table_pcl.getCellData(i).equals(pclString))
			{
				return (short) Integer.parseInt(table_code.getCellData(i), 16);
			}
		}
		return 0;
	}
}
