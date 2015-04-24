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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SingleLayoutController {

	private ObservableList<ValueClass> data = FXCollections.observableArrayList();
	private ObservableList<ValueClassSpeicher> data_speicher = FXCollections.observableArrayList();
	public static int programcounter = 0;
	public static int [] tos = new int [8];
	public static short tos_counter = 0;
	
	
	public static void writeCounter()
	{
		tos[tos_counter] = programcounter+1;
		tos_counter ++;
		tos_counter = (short) (tos_counter & 7);
	}
	public static void getCounter()
	{
		programcounter = tos[tos_counter];
		tos_counter--;
		if(tos_counter < 0)
		{
			tos_counter = 7;
		}
	}
	

	private Thread t = null;

	@FXML
	private MenuItem openFile;

	// Tabelle fuer Code
	@FXML
	private TableView<ValueClass> table;

	// Speicher Tabelle
	@FXML
	private TableView<ValueClassSpeicher> speicher;

	@FXML
	private Button button_start;

	@FXML
	private Button button_stop;
	
	@FXML
	private Button button_pause;
	
	// Spalten erzeugen
	TableColumn<ValueClass, String> table_pcl = new TableColumn<ValueClass, String>("PCL");
	TableColumn<ValueClass, String> table_code = new TableColumn<ValueClass, String>("Code");
	TableColumn<ValueClass, String> table_zusatz = new TableColumn<ValueClass, String>("Zusatz");

	// Spalten fuer Speicher Tabelle
	TableColumn<ValueClassSpeicher, String> table_zahl = new TableColumn<ValueClassSpeicher, String>(" ");
	TableColumn<ValueClassSpeicher, String> table_00 = new TableColumn<ValueClassSpeicher, String>("00");
	TableColumn<ValueClassSpeicher, String> table_01 = new TableColumn<ValueClassSpeicher, String>("01");
	TableColumn<ValueClassSpeicher, String> table_02 = new TableColumn<ValueClassSpeicher, String>("02");
	TableColumn<ValueClassSpeicher, String> table_03 = new TableColumn<ValueClassSpeicher, String>("03");
	TableColumn<ValueClassSpeicher, String> table_04 = new TableColumn<ValueClassSpeicher, String>("04");
	TableColumn<ValueClassSpeicher, String> table_05 = new TableColumn<ValueClassSpeicher, String>("05");
	TableColumn<ValueClassSpeicher, String> table_06 = new TableColumn<ValueClassSpeicher, String>("06");
	TableColumn<ValueClassSpeicher, String> table_07 = new TableColumn<ValueClassSpeicher, String>("07");

	// File oeffnen
	@FXML
	public void onClickOpenFile() {

		// Charset fuer Umlaute
		Charset charset = Charset.forName("ISO-8859-1");

		FileChooser fileChooser = new FileChooser();

		Stage primaryStage = (Stage) table.getScene().getWindow();

		fileChooser.setTitle("Choose a File");
		Path file = Paths.get(fileChooser.showOpenDialog(primaryStage)
				.getPath());

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
			for (String line : lines) {
				data.add(new ValueClass(line.substring(0, 4).trim(), line
						.substring(5, 9).trim(), line.substring(10,
						line.length()).trim()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		table.setItems(data);
		table.getColumns().add(table_pcl);
		table.getColumns().add(table_code);
		table.getColumns().add(table_zusatz);

		// Speichertabelle in Tab Sonstiges anlegen
		erzeugeSpeicher();
	}

	// Dokumentation oeffnen
	// Doku.pdf muss im bin Ordner des Programms liegen
	@FXML
	public void onClickDoku() {
		// Dateipfad des Programms erhalten, %20 durch Leerzeichen ersetzen und
		// \Doku.pdf anfuegen
		File file = new File(getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath().replaceAll("%20", " ")
				+ "/Doku.pdf");

		try {
			Desktop desktop = Desktop.getDesktop();
			desktop.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public short getLine() {
		String pclString;
		if (programcounter < 16)
			pclString = "000"
					+ Integer.toHexString(programcounter).toUpperCase();
		else if (programcounter < 256)
			pclString = "00"
					+ Integer.toHexString(programcounter).toUpperCase();
		else if (programcounter < 4096)
			pclString = "0" + Integer.toHexString(programcounter).toUpperCase();
		else
			pclString = Integer.toHexString(programcounter).toUpperCase();
		for (int i = 0; i <= table.getItems().size(); i++) {
			if (table_pcl.getCellData(i).equals(pclString)) {
				final int row = i;
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						updateUiTable(row);
						
					}
				});
				
				programcounter ++;
				return (short) Integer.parseInt(table_code.getCellData(i), 16);
			}
		}
		return 0;
	}

	public void erzeugeSpeicher() {
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

		for (int i = 0; i <= 31; i++) {
			if (i * 8 < 16)
				zeileNummer = "0" + Integer.toHexString(i * 8).toUpperCase();
			else
				zeileNummer = Integer.toHexString(i * 8).toUpperCase();
			data_speicher.add(new ValueClassSpeicher(zeileNummer, "00", "00",
					"00", "00", "00", "00", "00", "00"));
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

	// Tabelle von oben bis unten abarbeiten
	public void startRunningThread() {
		BefehlDecoder decoder = new BefehlDecoder();
		
		if (t == null) {

			// Task um die UI Funktionalität nicht zu blockieren
			Runnable task = new Runnable() {
				@Override
				public void run() {
					short max_pcl = 0;

					for (int i = 0; i < table.getItems().size(); i++)
					{
						if (!table_pcl.getCellData(i).equals("")) {
							 max_pcl = (short) Integer.parseInt(table_pcl.getCellData(i), 16);
						}
					}
					System.out.println("Max. PCL: " + max_pcl + "\n\n");
					for (int i = 0; i <= max_pcl; i++)
					{
						System.out.println("PCL: " + programcounter+ "  Code: " + getLine() + "\n");
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					t = null;
					programcounter = 0;
				};
			};
			t = new Thread(task);
			t.start();
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fehler beim Starten");
			alert.setHeaderText("Das Programm wird bereits ausgefuehrt");
			alert.setContentText("Sie haben das Programm bereits gestartet.\n"
					+ "Beenden Sie erst das laufende Programm und starten Sie es dann erneut.");
			alert.showAndWait();
		}
	}

	// Befehlscode einlesen und zur Verarbeitung weitergeben
	public void readCode(int row) {
		
		// wenn table_pcl leer ist, dann passiert hier nichts
		if (!table_pcl.getCellData(row).equals("")) {
			String befehlText = table_code.getCellData(row).toUpperCase();
			short befehlCode = (short) Integer.parseInt(befehlText, 16);
			
			System.out.println("Befehlscode: " + befehlCode + " ProgrammCounter: " + programcounter);
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			programcounter++;
		}
	}
	
	private void updateUiTable(int row){
		table.getSelectionModel().select(row);
		table.getFocusModel().focus(row);
		table.scrollTo(row);
	}
	
	@SuppressWarnings("deprecation")
	public void stopRunningThread(){
		if (t != null) {
			t.stop();
			t = null;
			programcounter = 0;
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fehler beim Stoppen");
			alert.setHeaderText("Kein Programm gestartet");
			alert.setContentText("Zur Zeit läuft kein Programm.\n"
					+ "Starten Sie ein Programm um es beenden zu können.");
			alert.showAndWait();
		}
	}
	
	@SuppressWarnings({ "deprecation", "static-access" })
	public void pauseRunningThread(){
		Alert alert = new Alert(AlertType.ERROR);
		String title = "Fehler beim Pausieren";
		String header = "Programm konnte nicht angehalten werden";
		String content = "";
		
		alert.setTitle(title);
		alert.setHeaderText(header);
		
		if (t == null) {
			content = "Das Programm wurde nicht gestartet.";
			alert.setContentText(content);
			alert.showAndWait();
		}
		else if (t.currentThread().getState() == Thread.State.TERMINATED) {
			content = "Das Programm wurde bereits beendet.";
			alert.setContentText(content);
			alert.showAndWait();
		}
		else {
			t.suspend();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void resumeRunningThread(){
		t.resume();
	}
}
