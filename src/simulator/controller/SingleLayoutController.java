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
import simulator.ValueClassSpeicher;
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
	private ObservableList<ValueClassSpeicher> data_speicher = FXCollections.observableArrayList();
	private int programcounter = 0;
	
	@FXML
	private MenuItem openFile;
	
	//Tabelle fuer Code
	@FXML
	private TableView<ValueClass> table;
	
	//Speicher Tabelle
	@FXML
	private TableView<ValueClassSpeicher> speicher;
/*	
	@FXML
	private TableColumn table_zahl;
	@FXML
	private TableColumn table_00;
	@FXML
	private TableColumn table_01;
	@FXML
	private TableColumn table_02;
	@FXML
	private TableColumn table_03;
	@FXML
	private TableColumn table_04;
	@FXML
	private TableColumn table_05;
	@FXML
	private TableColumn table_06;
	@FXML
	private TableColumn table_07;
*/	
	//Spalten erzeugen
	TableColumn<ValueClass, String> table_pcl = new TableColumn<ValueClass, String>("PCL");
	TableColumn<ValueClass, String> table_code = new TableColumn<ValueClass, String>("Code");
	TableColumn<ValueClass, String> table_zusatz = new TableColumn<ValueClass, String>("Zusatz");
	
	//Spalten fuer Speicher Tabelle
	TableColumn<ValueClassSpeicher, String> table_zahl = new TableColumn<ValueClassSpeicher, String>(" ");
	TableColumn<ValueClassSpeicher, String> table_00 = new TableColumn<ValueClassSpeicher, String>("00");
	TableColumn<ValueClassSpeicher, String> table_01 = new TableColumn<ValueClassSpeicher, String>("01");
	TableColumn<ValueClassSpeicher, String> table_02 = new TableColumn<ValueClassSpeicher, String>("02");
	TableColumn<ValueClassSpeicher, String> table_03 = new TableColumn<ValueClassSpeicher, String>("03");
	TableColumn<ValueClassSpeicher, String> table_04 = new TableColumn<ValueClassSpeicher, String>("04");
	TableColumn<ValueClassSpeicher, String> table_05 = new TableColumn<ValueClassSpeicher, String>("05");
	TableColumn<ValueClassSpeicher, String> table_06 = new TableColumn<ValueClassSpeicher, String>("06");
	TableColumn<ValueClassSpeicher, String> table_07 = new TableColumn<ValueClassSpeicher, String>("07");
	
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		table.setItems(data);
		table.getColumns().add(table_pcl);
		table.getColumns().add(table_code);
		table.getColumns().add(table_zusatz);
		
		//Speichertabelle in Tab Sonstiges anlegen
		erzeugeSpeicher();
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
	
	public void erzeugeSpeicher()
	{
		String zeileNummer;
		
		table_zahl.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_zahl"));
		table_zahl.setEditable(true);
		table_zahl.setSortable(false);
		table_zahl.setMinWidth(25);
		table_zahl.setMaxWidth(25);
		
		table_00.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_00"));
		table_00.setEditable(true);
		table_00.setSortable(false);
		table_00.setMinWidth(25);
		table_00.setMaxWidth(25);
		
		table_01.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_01"));
		table_01.setEditable(true);
		table_01.setSortable(false);
		table_01.setMinWidth(25);
		table_01.setMaxWidth(25);
		
		table_02.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_02"));
		table_02.setEditable(true);
		table_02.setSortable(false);
		table_02.setMinWidth(25);
		table_02.setMaxWidth(25);
		
		table_03.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_03"));
		table_03.setEditable(true);
		table_03.setSortable(false);
		table_03.setMinWidth(25);
		table_03.setMaxWidth(25);
		
		table_04.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_04"));
		table_04.setEditable(true);
		table_04.setSortable(false);
		table_04.setMinWidth(25);
		table_04.setMaxWidth(25);
		
		table_05.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_05"));
		table_05.setEditable(true);
		table_05.setSortable(false);
		table_05.setMinWidth(25);
		table_05.setMaxWidth(25);
		
		table_06.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_06"));
		table_06.setEditable(true);
		table_06.setSortable(false);
		table_06.setMinWidth(25);
		table_06.setMaxWidth(25);
		
		table_07.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_07"));
		table_07.setEditable(true);
		table_07.setSortable(false);
		table_07.setMinWidth(25);
		table_07.setMaxWidth(25);
		
		for(int i = 0; i <= 31; i++)
		{
			if(i*8 < 16)
				zeileNummer = "0" + Integer.toHexString(i*8).toUpperCase();
			else
				zeileNummer = Integer.toHexString(i*8).toUpperCase();
			data_speicher.add(new ValueClassSpeicher(zeileNummer, "00", "00", "00", "00", "00", "00", "00", "00"));		
		}
	speicher.setItems(data_speicher);
	speicher.getColumns().add(table_zahl);
	speicher.getColumns().add(table_00);
	speicher.getColumns().add(table_01);
	speicher.getColumns().add(table_02);
	speicher.getColumns().add(table_03);
	speicher.getColumns().add(table_04);
	speicher.getColumns().add(table_05);
	speicher.getColumns().add(table_06);
	speicher.getColumns().add(table_07);
	}
	
	
}
