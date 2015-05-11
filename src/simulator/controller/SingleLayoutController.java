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
import simulator.Interrupt;
import simulator.Laufzeit;
import simulator.PortA;
import simulator.PortB;
import simulator.Programcounter;
import simulator.RegisterAdressen;
import simulator.SeriellerPort;
import simulator.Stack;
import simulator.SyncRegister;
import simulator.Timer0;
import simulator.TrisA;
import simulator.TrisB;
import simulator.ValueClass;
import simulator.ValueClassSpeicher;
import simulator.Watchdog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SingleLayoutController {

	private ObservableList<ValueClass> data = FXCollections.observableArrayList();
	private ObservableList<ValueClassSpeicher> data_speicher = FXCollections.observableArrayList();
	//public static int programcounter = 0;
	public static int taktzyklen = 0;
	private int maximalerPC = 0;
	public static boolean pause = false;
	public static boolean schritt = false;
	private boolean ersterStart = true;
	public static boolean WDTReset = false;
	
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
	@FXML
	private Button button_schritt;
	@FXML
	private Label label_wRegister;
	@FXML
	private Label label_pcl;
	@FXML
	private Label label_fsr;
	@FXML
	private Label label_pclath;
	@FXML
	private Label label_status;
	@FXML
	private Label label_programcounter;
	@FXML
	private Label label_taktzyklen;
	@FXML
	private Label label_laufzeit;
	
	@SuppressWarnings("rawtypes")
	@FXML
	public ChoiceBox choice_quarzfrequenz;
	
	//RA0 Register mit TrisA
	@FXML
	private Label label_trisa_0;
	@FXML
	private Label label_trisa_1;
	@FXML
	private Label label_trisa_2;
	@FXML
	private Label label_trisa_3;
	@FXML
	private Label label_trisa_4;
	@FXML
	private Label label_trisa_5;
	@FXML
	private Label label_trisa_6;
	@FXML
	private Label label_trisa_7;
	@FXML
	private Label label_pina_0;
	@FXML
	private Label label_pina_1;
	@FXML
	private Label label_pina_2;
	@FXML
	private Label label_pina_3;
	@FXML
	private Label label_pina_4;
	@FXML
	private Label label_pina_5;
	@FXML
	private Label label_pina_6;
	@FXML
	private Label label_pina_7;
	
	
	//RB0 Register mit TrisB
	@FXML
	private Label label_trisb_0;
	@FXML
	private Label label_trisb_1;
	@FXML
	private Label label_trisb_2;
	@FXML
	private Label label_trisb_3;
	@FXML
	private Label label_trisb_4;
	@FXML
	private Label label_trisb_5;
	@FXML
	private Label label_trisb_6;
	@FXML
	private Label label_trisb_7;
	@FXML
	private Label label_pinb_0;
	@FXML
	private Label label_pinb_1;
	@FXML
	private Label label_pinb_2;
	@FXML
	private Label label_pinb_3;
	@FXML
	private Label label_pinb_4;
	@FXML
	private Label label_pinb_5;
	@FXML
	private Label label_pinb_6;
	@FXML
	private Label label_pinb_7;
	
	//Stack Label's
	@FXML
	private Label stack_0;
	@FXML
	private Label stack_1;
	@FXML
	private Label stack_2;
	@FXML
	private Label stack_3;
	@FXML
	private Label stack_4;
	@FXML
	private Label stack_5;
	@FXML
	private Label stack_6;
	@FXML
	private Label stack_7;
	
	//Drop Down
	@SuppressWarnings("rawtypes" )
	@FXML
	private ChoiceBox choice_hardware;
	
	
	
	// Spalten erzeugen
	TableColumn<ValueClass, String> table_checkbox = new TableColumn<ValueClass, String>("");
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
		
		if(ersterStart){
			
			table_checkbox.setCellValueFactory(new PropertyValueFactory<ValueClass, String>("text_checkbox"));
			table_checkbox.setEditable(false);
			table_checkbox.setSortable(false);
			table_checkbox.setMinWidth(25);
			table_checkbox.setMaxWidth(25);
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
			table.getColumns().add(table_checkbox);
			table.getColumns().add(table_pcl);
			table.getColumns().add(table_code);
			table.getColumns().add(table_zusatz);
			
			// Speichertabelle in Tab Sonstiges anlegen
			erzeugeSpeicher();
			
			//choiceBox fuer Quarzfrequenz fuellen
			erzeugeFrequenzChoice();
			
	setComPorts();
			
			ersterStart = false;
		}
		else{
			
			data.clear();
			
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
			
			//Speichertabelle aktualisieren
			aktualisiereSpeicherView();
			//TrisA Ansicht aktualisieren (i/o)
			aktualisiereTrisA();
			//TrisB Ansicht aktualisieren (i/o)
			aktualisiereTrisB();
			//PortA Ansicht aktualisieren
			aktualisierePortA();
			//PortB Ansicht aktualisiseren
			aktualisierePortB();
		}
		//Power-On-Reset durchfuehren
		powerOnReset();
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
		String pclString; //String fuer den PCL
		
		//Programcounter.pc String formatieren, sodass er immer 4-Stellig Hex angezeigt wird
		if (Programcounter.pc < 16)
			pclString = "000"
					+ Integer.toHexString(Programcounter.pc).toUpperCase();
		else if (Programcounter.pc < 256)
			pclString = "00"
					+ Integer.toHexString(Programcounter.pc).toUpperCase();
		else if (Programcounter.pc < 4096)
			pclString = "0" + Integer.toHexString(Programcounter.pc).toUpperCase();
		else
			pclString = Integer.toHexString(Programcounter.pc).toUpperCase();
		
		
		for (int i = 0; i <= table.getItems().size(); i++) {
			if (table_pcl.getCellData(i).equals(pclString)) {
				final int row = i;
				Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						updateUiTable(row);
						//Breakpoint Abfrage
						if (data.get(row).getText_checkbox().selectedProperty().get()) {
							pause = true;
							button_pause.setText("Resume");
						}
					}
				});
				
				return (short) Integer.parseInt(table_code.getCellData(i), 16);
			}
		}
		return 0;
	}

	//SPeichertabelle erzeugen
	public void erzeugeSpeicher() {
		String zeileNummer;

		table_zahl.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_zahl"));
		table_zahl.setEditable(true);
		table_zahl.setSortable(false);
		table_zahl.setStyle("-fx-background-color:#E2E2E2; -fx-border-color:#EDEDED; -fx-border-style: solid; -fx-border-width:0.5px");
		table_zahl.setMinWidth(25);
		table_zahl.setMaxWidth(25);

		table_00.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_00"));
		table_00.setEditable(true);
		table_00.setSortable(false);
		table_00.setMinWidth(26);
		table_00.setMaxWidth(26);

		table_01.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_01"));
		table_01.setEditable(true);
		table_01.setSortable(false);
		table_01.setMinWidth(26);
		table_01.setMaxWidth(26);

		table_02.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_02"));
		table_02.setEditable(true);
		table_02.setSortable(false);
		table_02.setMinWidth(26);
		table_02.setMaxWidth(26);

		table_03.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_03"));
		table_03.setEditable(true);
		table_03.setSortable(false);
		table_03.setMinWidth(26);
		table_03.setMaxWidth(26);

		table_04.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_04"));
		table_04.setEditable(true);
		table_04.setSortable(false);
		table_04.setMinWidth(26);
		table_04.setMaxWidth(26);

		table_05.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_05"));
		table_05.setEditable(true);
		table_05.setSortable(false);
		table_05.setMinWidth(26);
		table_05.setMaxWidth(26);

		table_06.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_06"));
		table_06.setEditable(true);
		table_06.setSortable(false);
		table_06.setMinWidth(26);
		table_06.setMaxWidth(26);

		table_07.setCellValueFactory(new PropertyValueFactory<ValueClassSpeicher, String>("text_07"));
		table_07.setEditable(true);
		table_07.setSortable(false);
		table_07.setMinWidth(26);
		table_07.setMaxWidth(26);

		//Speichertabelle mit 00 initialisieren
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
		pause = false;
		
		
		
		//Quarzfrequenz lesen
		getQuarzFrequenz();
		
		//ComPort lesen
		getComPortsChoice();
		
		if (t == null) {
			powerOnReset();
			// Task um die UI Funktionalität nicht zu blockieren
			Runnable task = new Runnable() {
				@Override
				public void run() {

					//Maximalen PCL berechnen
					for (int i = 0; i < table.getItems().size(); i++)
					{
						if (!table_pcl.getCellData(i).equals("")) {
							 maximalerPC = (short) Integer.parseInt(table_pcl.getCellData(i), 16);
						}
					}
					
					//Hauptschleife
					for (int i = 0; i <= maximalerPC; i = Programcounter.pc)
					{
						//Register synchronisieren
						SyncRegister.synchronisieren();
						
						//Speichertabelle als Ansicht Aktualisieren
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								//Speicher Tabelle aktualisieren
								aktualisiereSpeicherView();
								//Stack Tabelle Aktualisieren
								aktualisiereStackGUI();
								//TrisA Ansicht aktualisieren (i/o)
								aktualisiereTrisA();
								//TrisB Ansicht aktualisieren (i/o)
								aktualisiereTrisB();
								//PortA Ansicht aktualisieren
								aktualisierePortA();
								//PortB Ansicht aktualisiseren
								aktualisierePortB();
							}
						});
						
						//Felder in der GUI aktualisieren
						Platform.runLater(new Runnable() {
							
							@Override
							public void run() {
								felderAktualisieren();
							}
						});
						
						//Alter Wert der Register erhalten und ggf Interrupt Bits setzen
						Interrupt.getAlteWerte();

						//Warten, wenn Pause aktiviert ist
						if(pause){
							synchronized (t) {
								try {
									t.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
						
						//Abfrage fuer Einzelschritte
						if(schritt){
							schritt = false;
							pause = true;
						}
						
						short test = getLine();
						System.out.println("PC: " + Programcounter.pc+ "  Code: " + test + "\n");
						
						//Eigentliches Ausfuehren
						decoder.decode(test);
						
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						//Direkt nach Aufruf ausfuehren:
						
						//Alle Register neu aus dem Speicher in ihre Arrays laden
						SyncRegister.speicherInRegister();
						
						//Alle "doppelten" Register synchronisiseren
						SyncRegister.synchronisieren();
						
						//Neuer Wert der Register erhalten und ggf Interrupt Bits setzen
						Interrupt.getNeueWerte();
						
						//Laufzeit berechnen
						Laufzeit.berechneLaufzeit();
						
						//Auf Interrupt pruefen
						Interrupt.pruefeInterrupt();
						
						//Timer0 berechnen
						Timer0.berechneTimer0();
						
						//Watchdogtimer berechnen
						Watchdog.berechneWatchdog();
						
						//Watchdog Reset						
						if(WDTReset){
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									WDTReset = false;
									powerOnReset();
								}
							});
						}
					}
					
					//Programmende
					if(Programcounter.pc >= maximalerPC)
					{
						t = null;
						maximalerPC = 0;
					}
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
			Programcounter.pc = 0;
			pause = false;
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fehler beim Stoppen");
			alert.setHeaderText("Kein Programm gestartet");
			alert.setContentText("Zur Zeit laeuft kein Programm.\n"
					+ "Starten Sie ein Programm um es beenden zu koennen.");
			alert.showAndWait();
		}
	}
	
	@SuppressWarnings({"static-access" })
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
			if(pause){
				pause = false;
				button_pause.setText("Pause");
				synchronized (t) {
					t.notify();
				}
			}
			else{
				pause = true;
				button_pause.setText("Resume");
			}
		}
	}

	public void felderAktualisieren(){
		
		//BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_0] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_1] = (short) Programcounter.pcl;
		
		//Label wRegister aktualisieren
		label_wRegister.setText("0x" + Integer.toHexString(BefehlDecoder.wRegister).toUpperCase());
		//Label PCL aktualisieren
		label_pcl.setText("0x" + Integer.toHexString(BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_0]).toUpperCase());
		//Label PCLATH aktualisieren
		label_pclath.setText("0x" + Integer.toHexString(BefehlDecoder.speicherZellen[10]).toUpperCase());
		//Label FSR aktualisieren
		label_fsr.setText("0x" + Integer.toHexString(BefehlDecoder.speicherZellen[RegisterAdressen.ADR_FSR_0]).toUpperCase());
		//Label Programmcounter aktualisieren
		label_programcounter.setText("0x" + Integer.toHexString(Programcounter.pc).toUpperCase());
		//Label Status aktualisieren
		label_status.setText("0x" + Integer.toHexString(BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_0]).toUpperCase());
		//Label Taktzyklen aktualisieren
		label_taktzyklen.setText("0x" + Integer.toHexString(taktzyklen).toUpperCase());
		//Label Laufzeit aktualisieren
		label_laufzeit.setText(Laufzeit.laufzeit_string);
	}
	
	//OnClick fuer Pins von PortA
	@FXML
	public void onClickPinA0()
	{
		if(label_pina_0.getText().equals("0"))
			label_pina_0.setText("1");
		else
			label_pina_0.setText("0");
		PortA.setPin(0);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinA1()
	{
		if(label_pina_1.getText().equals("0"))
			label_pina_1.setText("1");
		else
			label_pina_1.setText("0");
		PortA.setPin(1);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinA2()
	{
		if(label_pina_2.getText().equals("0"))
			label_pina_2.setText("1");
		else
			label_pina_2.setText("0");
		PortA.setPin(2);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinA3()
	{
		if(label_pina_3.getText().equals("0"))
			label_pina_3.setText("1");
		else
			label_pina_3.setText("0");
		PortA.setPin(3);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinA4()
	{
		if(label_pina_4.getText().equals("0"))
			label_pina_4.setText("1");
		else
			label_pina_4.setText("0");
		PortA.setPin(4);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinA5()
	{
		if(label_pina_5.getText().equals("0"))
			label_pina_5.setText("1");
		else
			label_pina_5.setText("0");
		PortA.setPin(5);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinA6()
	{
		if(label_pina_6.getText().equals("0"))
			label_pina_6.setText("1");
		else
			label_pina_6.setText("0");
		PortA.setPin(6);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinA7()
	{
		if(label_pina_7.getText().equals("0"))
			label_pina_7.setText("1");
		else
			label_pina_7.setText("0");
		PortA.setPin(7);
		aktualisiereSpeicherView();
	}
	
	//OnClick fuer Pins von PortB
	@FXML
	public void onClickPinB0()
	{
		if(label_pinb_0.getText().equals("0"))
			label_pinb_0.setText("1");
		else
			label_pinb_0.setText("0");
		PortB.setPin(0);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinB1()
	{
		if(label_pinb_1.getText().equals("0"))
			label_pinb_1.setText("1");
		else
			label_pinb_1.setText("0");
		PortB.setPin(1);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinB2()
	{
		if(label_pinb_2.getText().equals("0"))
			label_pinb_2.setText("1");
		else
			label_pinb_2.setText("0");
		PortB.setPin(2);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinB3()
	{
		if(label_pinb_3.getText().equals("0"))
			label_pinb_3.setText("1");
		else
			label_pinb_3.setText("0");
		PortB.setPin(3);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinB4()
	{
		if(label_pinb_4.getText().equals("0"))
			label_pinb_4.setText("1");
		else
			label_pinb_4.setText("0");
		PortB.setPin(4);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinB5()
	{
		if(label_pinb_5.getText().equals("0"))
			label_pinb_5.setText("1");
		else
			label_pinb_5.setText("0");
		PortB.setPin(5);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinB6()
	{
		if(label_pinb_6.getText().equals("0"))
			label_pinb_6.setText("1");
		else
			label_pinb_6.setText("0");
		PortB.setPin(6);
		aktualisiereSpeicherView();
	}
	@FXML
	public void onClickPinB7()
	{
		if(label_pinb_7.getText().equals("0"))
			label_pinb_7.setText("1");
		else
			label_pinb_7.setText("0");
		PortB.setPin(7);
		aktualisiereSpeicherView();
	}
	
	//Funktion fuer das Aktualisieren der Speichertabelle
	public void aktualisiereSpeicherView(){
		String zeileNummer;
		data_speicher.clear();
		
		for (int i = 0; i <= 31; i++) {
			String[] speicherByte = new String[8];
			for(int a = 0; a < 8; a++){
				speicherByte[a] = Integer.toHexString(BefehlDecoder.speicherZellen[(8*i)+a]).toUpperCase();
				if(speicherByte[a].length() <= 1){
					speicherByte[a] = "0" + speicherByte[a];
				}
			}
			
			if (i * 8 < 16)
				zeileNummer = "0" + Integer.toHexString(i * 8).toUpperCase();
			else
				zeileNummer = Integer.toHexString(i * 8).toUpperCase();
			
			data_speicher.add(new ValueClassSpeicher(
				zeileNummer, 
				speicherByte[0],
				speicherByte[1],
				speicherByte[2],
				speicherByte[3],
				speicherByte[4],
				speicherByte[5],
				speicherByte[6],
				speicherByte[7]
			));
		}
		PortA.setPortA();
		PortB.setPortB();
	}
	
	public void powerOnReset(){
		Programcounter.pc = 0;
		Programcounter.pcl = 0;
		Programcounter.pclath = 0;
		Watchdog.watchdog = 0;
		
		Interrupt.pclAlt = 0;
		Interrupt.pclNeu = 0;
		Interrupt.cyclesAlt = 0;
		Interrupt.taktzyklenAlt = 0;
		Interrupt.taktzyklenNeu = 0;
		
		taktzyklen = 0;
		Laufzeit.laufzeit = 0;
		Stack.clearStack();
		
		//Spezielle Register nach Schema: Bank0 = Bank1 = Wert
		
		//Adr 0x0 - kein physikalisches Register - als 0
		BefehlDecoder.speicherZellen[0] = 0;
		//Adr 0x80 - kein physikalisches Register - als 0
		BefehlDecoder.speicherZellen[128] = 0;
		
		//TMR0 unknown, NUR AUF BANK 0!
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TMR0] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TMR0] & 255);
		//OPTION_REG, NUR AUF BANK 1!
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_OPTIONREG] = 255;
		//PCL
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_0] = 0;
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_1] = 0;
		//STATUS
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_0] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_0] | 24);
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_1] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_1] | 24);
		//FSR unknown
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_FSR_0] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_FSR_0] & 255);
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_FSR_1] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_FSR_1] & 255);
		//PortA, NUR AUF BANK 0!
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTA] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTA] & 31);
		//TrisA, NUR AUF BANK 1!
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TRISA] = 31;
		//PortB unknown, NUR AUF BANK 0!
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTB] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTB] & 255);
		//TrisB, NUR AUF BANK 1!
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TRISB] = 255;
		
		//Unimplemented Adr 0x7 als 0
		BefehlDecoder.speicherZellen[7] = 0;
		//Unimplemented Adr 0x87 als 0
		BefehlDecoder.speicherZellen[135] = 0;
		
		//EEDATA unknown, NUR AUF BANK 0!
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_EEDATA] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_EEDATA] & 255);
		//EECON1, NUR AUF BANNK 1!
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_EECON1] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_EECON1] & 8);
		//EEADR unknown, NUR AUF BANK 0!
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_EEADR] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_EEADR] & 255);
		//EECON2, NUR AUF BANNK 1! kein physikalisches Register - als 0
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_EECON2] = 0;
		//PCLATH
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCLATH_0] = 0;
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCLATH_1] = 0;
		//INTCON
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_0] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_0] & 1);
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_1] = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_1] & 1);
	
		//Register Arrays aus Speicher laden
		SyncRegister.speicherInRegister();
		//Register synchronisieren
		SyncRegister.synchronisieren();
		
		//Felder der ersten View aktualisiseren (wRegister, ... )
		felderAktualisieren();
		//Speichertabellen View aktualisiseren
		aktualisiereSpeicherView();
		//TrisA Ansicht aktualisieren (i/o)
		aktualisiereTrisA();
		//TrisB Ansicht aktualisieren (i/o)
		aktualisiereTrisB();
		//PortA Ansicht aktualisieren
		aktualisierePortA();
		//PortB Ansicht aktualisiseren
		aktualisierePortB();
	}
	
	@SuppressWarnings("static-access")
	@FXML
	public void onClickSchritt() {
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
			if(pause){
				schritt = true;
				pause = false;
				synchronized (t) {
					t.notify();
				}
			}
		}
	}
	
	//Erzeugt die Auswahl der Frequenzen
	@SuppressWarnings("unchecked")
	public void erzeugeFrequenzChoice(){
		choice_quarzfrequenz.setItems(FXCollections.observableArrayList(
			    "32.76800 kHz",
			    "500.0000 kHz",
			    "1.000000 MHz",
			    "2.000000 MHz",
			    "2.457600 MHz",
			    "3.000000 MHz",
			    "3.276800 MHz",
			    "3.680000 MHz",
			    "3.686411 MHz",
			    "4.000000 MHz",
			    "4.096000 MHz",
			    "4.194304 MHz",
			    "4.433619 MHz",
			    "4.915200 MHz",
			    "5.000000 MHz",
			    "6.000000 MHz",
			    "6.144000 MHz",
			    "6.250000 MHz",
			    "6.553600 MHz",
			    "8.000000 MHz",
			    "10.00000 MHz",
			    "12.00000 MHz",
			    "16.00000 MHz",
			    "20.00000 MHz",
			    "24.00000 MHz",
			    "32.00000 MHz",
			    "40.00000 MHz",
			    "80.00000 MHz")
			);
		choice_quarzfrequenz.getSelectionModel().selectFirst();
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	public void setComPorts(){
		
		List<String> comPorts = SeriellerPort.getAllPorts();		
		ObservableList<String> availablePorts = FXCollections.observableArrayList(comPorts);
		
		choice_hardware.setItems(availablePorts);
	}
	
	//Wird beim Start druecken aufgerufen
	private void getQuarzFrequenz(){
		Laufzeit.einheit = ((String) choice_quarzfrequenz.getValue()).substring(9, 12);
		Laufzeit.quarzfrequenz = Double.parseDouble(((String) (choice_quarzfrequenz.getValue())).substring(0, 8));
	}
	
	@FXML
	public void getComPortsChoice(){
		
		String comPort;
		
		comPort = (String) choice_hardware.getValue();
		
		//TODO: Hardwareansteuerung!
		
	}
	
	private void aktualisiereStackGUI(){
		
		stack_0.setText(Integer.toHexString(Stack.stack[0]).toUpperCase());
		stack_1.setText(Integer.toHexString(Stack.stack[1]).toUpperCase());
		stack_2.setText(Integer.toHexString(Stack.stack[2]).toUpperCase());
		stack_3.setText(Integer.toHexString(Stack.stack[3]).toUpperCase());
		stack_4.setText(Integer.toHexString(Stack.stack[4]).toUpperCase());
		stack_5.setText(Integer.toHexString(Stack.stack[5]).toUpperCase());
		stack_6.setText(Integer.toHexString(Stack.stack[6]).toUpperCase());
		stack_7.setText(Integer.toHexString(Stack.stack[7]).toUpperCase());
		
		switch (Stack.stack_pointer) {
		case 0:
			setStackFont();
			stack_0.setTextFill(Color.RED);
			break;
		case 1:
			setStackFont();
			stack_1.setTextFill(Color.RED);
			break;
		case 2:
			setStackFont();
			stack_2.setTextFill(Color.RED);
			break;
		case 3:
			setStackFont();
			stack_3.setTextFill(Color.RED);
			break;
		case 4:
			setStackFont();
			stack_4.setTextFill(Color.RED);
			break;
		case 5:
			setStackFont();
			stack_5.setTextFill(Color.RED);
			break;
		case 6:
			setStackFont();
			stack_6.setTextFill(Color.RED);
			break;
		case 7:
			setStackFont();
			stack_7.setTextFill(Color.RED);
			break;
		default:
			break;
		}
		
	}
	
	private void setStackFont(){
		stack_0.setTextFill(Color.BLACK);
		stack_1.setTextFill(Color.BLACK);
		stack_2.setTextFill(Color.BLACK);
		stack_3.setTextFill(Color.BLACK);
		stack_4.setTextFill(Color.BLACK);
		stack_5.setTextFill(Color.BLACK);
		stack_6.setTextFill(Color.BLACK);
		stack_7.setTextFill(Color.BLACK);
	}
	
	private void aktualisiereTrisA(){
		//TrisA Ansicht aktualisieren
		//Ist ein Pin gesetzt (1), dann ist dieser Pin ein Input
		//Ist ein Pin cleared (0), dann ist dieser Pin ein Output
		
		//TrisA[0]
		if(TrisA.trisA[0]){
			label_trisa_0.setText("i");
		}
		else{
			label_trisa_0.setText("o");
		}
		
		//TrisA[1]
		if(TrisA.trisA[1]){
			label_trisa_1.setText("i");
		}
		else{
			label_trisa_1.setText("o");
		}
		
		//TrisA[2]
		if(TrisA.trisA[2]){
			label_trisa_2.setText("i");
		}
		else{
			label_trisa_2.setText("o");
		}
		
		//TrisA[3]
		if(TrisA.trisA[3]){
			label_trisa_3.setText("i");
		}
		else{
			label_trisa_3.setText("o");
		}
		
		//TrisA[4]
		if(TrisA.trisA[4]){
			label_trisa_4.setText("i");
		}
		else{
			label_trisa_4.setText("o");
		}
		
		//TrisA[5]
		if(TrisA.trisA[5]){
			label_trisa_5.setText("i");
		}
		else{
			label_trisa_5.setText("o");
		}
		
		//TrisA[6]
		if(TrisA.trisA[6]){
			label_trisa_6.setText("i");
		}
		else{
			label_trisa_6.setText("o");
		}
		
		//TrisA[7]
		if(TrisA.trisA[7]){
			label_trisa_7.setText("i");
		}
		else{
			label_trisa_7.setText("o");
		}
	}
	
	
	private void aktualisiereTrisB(){
		//TrisB Ansicht aktualisieren
		//Ist ein Pin gesetzt (1), dann ist dieser Pin ein Input
		//Ist ein Pin cleared (0), dann ist dieser Pin ein Output
		
		//TrisB[0]
		if(TrisB.trisB[0]){
			label_trisb_0.setText("i");
		}
		else{
			label_trisb_0.setText("o");
		}
		
		//TrisB[1]
		if(TrisB.trisB[1]){
			label_trisb_1.setText("i");
		}
		else{
			label_trisb_1.setText("o");
		}
		
		//TrisB[2]
		if(TrisB.trisB[2]){
			label_trisb_2.setText("i");
		}
		else{
			label_trisb_2.setText("o");
		}
		
		//TrisB[3]
		if(TrisB.trisB[3]){
			label_trisb_3.setText("i");
		}
		else{
			label_trisb_3.setText("o");
		}
		
		//TrisB[4]
		if(TrisB.trisB[4]){
			label_trisb_4.setText("i");
		}
		else{
			label_trisb_4.setText("o");
		}
		
		//TrisB[5]
		if(TrisB.trisB[5]){
			label_trisb_5.setText("i");
		}
		else{
			label_trisb_5.setText("o");
		}
		
		//TrisB[6]
		if(TrisB.trisB[6]){
			label_trisb_6.setText("i");
		}
		else{
			label_trisb_6.setText("o");
		}
		
		//TrisB[7]
		if(TrisB.trisB[7]){
			label_trisb_7.setText("i");
		}
		else{
			label_trisb_7.setText("o");
		}
	}
	
	
	private void aktualisierePortA(){
		//PortA Ansicht aktualisieren
		
		//PortA[0]
		if(PortA.portA[0]){
			label_pina_0.setText("1");
		}
		else{
			label_pina_0.setText("0");
		}
		
		//PortA[1]
		if(PortA.portA[1]){
			label_pina_1.setText("1");
		}
		else{
			label_pina_1.setText("0");
		}
		
		//PortA[2]
		if(PortA.portA[2]){
			label_pina_2.setText("1");
		}
		else{
			label_pina_2.setText("0");
		}
		
		//PortA[3]
		if(PortA.portA[3]){
			label_pina_3.setText("1");
		}
		else{
			label_pina_3.setText("0");
		}
		
		//PortA[4]
		if(PortA.portA[4]){
			label_pina_4.setText("1");
		}
		else{
			label_pina_4.setText("0");
		}
		
		//PortA[5]
		if(PortA.portA[5]){
			label_pina_5.setText("1");
		}
		else{
			label_pina_5.setText("0");
		}
		
		//PortA[6]
		if(PortA.portA[6]){
			label_pina_6.setText("1");
		}
		else{
			label_pina_6.setText("0");
		}
		
		//PortA[7]
		if(PortA.portA[7]){
			label_pina_7.setText("1");
		}
		else{
			label_pina_7.setText("0");
		}
	}
	
	
	private void aktualisierePortB(){
		//PortB Ansicht aktualisieren
		
		//PortB[0]
		if(PortB.portB[0]){
			label_pinb_0.setText("1");
		}
		else{
			label_pinb_0.setText("0");
		}
		
		//PortB[1]
		if(PortB.portB[1]){
			label_pinb_1.setText("1");
		}
		else{
			label_pinb_1.setText("0");
		}
		
		//PortB[2]
		if(PortB.portB[2]){
			label_pinb_2.setText("1");
		}
		else{
			label_pinb_2.setText("0");
		}
		
		//PortB[3]
		if(PortB.portB[3]){
			label_pinb_3.setText("1");
		}
		else{
			label_pinb_3.setText("0");
		}
		
		//PortB[4]
		if(PortB.portB[4]){
			label_pinb_4.setText("1");
		}
		else{
			label_pinb_4.setText("0");
		}
		
		//PortB[5]
		if(PortB.portB[5]){
			label_pinb_5.setText("1");
		}
		else{
			label_pinb_5.setText("0");
		}
		
		//PortB[6]
		if(PortB.portB[6]){
			label_pinb_6.setText("1");
		}
		else{
			label_pinb_6.setText("0");
		}
		
		//PortB[7]
		if(PortB.portB[7]){
			label_pinb_7.setText("1");
		}
		else{
			label_pinb_7.setText("0");
		}
	}
}