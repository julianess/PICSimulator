package simulator;

import java.text.DecimalFormat;

//Klasse fuer alles, was die Laufzeit betrifft

public class Laufzeit {
	public static double quarzfrequenz = 0; //Quarzfrequenz
	public static double laufzeit = 0; //Laufzeit
	public static String einheit = ""; //Erhaelt die Einheit von der Auswahl
	public static int taktzyklen_vorAufruf = 0;		//Taktzyklen vor und nach dem Aufruf
	public static int taktzyklen_nachAufruf = 0;	//Differenz = verbrauchte Taktzyklen
	public static String laufzeit_string = ""; //String fuer die Ausgabe der Laufzeit
	
	private static double divisor = 0; //Divisor fuer die Laufzeitberechnung
	
	//Laufzeit berechnen und in einen Ausgabestring formatieren
	public static void berechneLaufzeit(){
		
		//Einheit ermitteln und Divisor fuer die Laufzeitberechnung berechnen
		if(einheit.equals("kHz")){
			divisor = quarzfrequenz * Math.pow(10,3);
		}
		else{
			divisor = quarzfrequenz * Math.pow(10, 6);
		}
		
		//Laufzeit berechnen, bzw. wenn Quarzfrenquenz = 0, dann Laufzeit = 0
		if(quarzfrequenz == 0){
			laufzeit = 0;
		}
		else{
			laufzeit += ((taktzyklen_nachAufruf - taktzyklen_vorAufruf)/divisor);
		}
		
		//Formatiere die Laufzeit auf fix 10 Nachkommastellen
		DecimalFormat df_laufzeit = new DecimalFormat("0.00000000");
		laufzeit_string = df_laufzeit.format(laufzeit) + " s";
	}
}
