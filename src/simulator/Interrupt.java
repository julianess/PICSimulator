package simulator;

//Diese Klasse implementiert die Interrupt Funktionen des PIC

import simulator.controller.SingleLayoutController;

public class Interrupt {
	
	//Variablen zum pruefen verschiedener Bestandteile auf eine Aenderung
	
	//PortB
	private static short portBAlt = 0;
	private static short portBNeu = 0;
	
	//RB0
	private static short rb0intAlt = 0;
	private static short rb0intNeu = 0;
	
	//Timer0
	private static short timer0Alt = 0;
	private static short timer0Neu = 0;
	
	//Taktzyklen
	public static short taktzyklenAlt = 0;
	public static short taktzyklenNeu = 0;
	
	//PCL
	public static short pclAlt = 0;
	public static short pclNeu = 0;
	
	
	//Speichert die Taktzyklen zum Zeitpunkt eines Interrupts (nach Interrupt +2)
	public static int cyclesAlt = 0;
	
	//Auf alle Interrupts pruefen
	public static void pruefeInterrupt(){
		//Nur Interrupt moeglich, wenn GIE Bit gesetzt
		if(Intcon.getGIE()){
			//Timer 0 Interrupt moeglich, wenn T0IE Bit gesetzt
			if(Intcon.getT0IE()){
				if(Intcon.getT0IF()){
					interruptStarten();
				}
			}
			
			//RB Interrupt
			if(Intcon.getRBIE()){
				if(Intcon.getRBIF()){
					interruptStarten();
				}
			}
			
			//RB0/INT
			if(Intcon.getINTE()){
				if(Intcon.getINTF()){
					interruptStarten();
				}
			}
		}
	}
	
	//Alte Werte der verschiedenen Faktoren fuer einen spaeteren Vergleich erhalten
	public static void getAlteWerte(){
		portBAlt = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTB] & 240);
		rb0intAlt = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTB] & 1);
		timer0Alt = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TMR0] & 255);
		taktzyklenAlt = (short) SingleLayoutController.taktzyklen;
		pclAlt = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_0] & 255);
	}
	
	//Neue Werte verschiedener Faktoren fuer den Vergleich erhalten und Vergleich durchfuehren
	public static void getNeueWerte(){
		portBNeu = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTB] & 240);
		rb0intNeu = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTB] & 1);
		timer0Neu = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TMR0] & 255);
		taktzyklenNeu = (short) SingleLayoutController.taktzyklen;
		pclNeu = (short) (BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_0] & 255);
		
		//Bei Aenderung RBIF Bit setzen
		if(portBNeu != portBAlt){
			Intcon.setRBIF(true);
		}
		
		//Bei Aenderung des rb0/int pruefen, ob steigende oder fallende Flanke
		if(rb0intNeu != rb0intAlt){
			
			//Steigende Flanke
			if((rb0intNeu > rb0intAlt) && OptionRegister.getINTEDG()){
				Intcon.setINTF(true);
			}
			//Fallende Flanke
			else if((rb0intNeu < rb0intAlt) && !OptionRegister.getINTEDG()){
				Intcon.setINTF(true);
			}
		}
		
		//Nur, wenn der Prescaler TMR0 zugewiesen ist
		if(!OptionRegister.getPSA()){
			//Wird der Timer direkt gesetzt, wird der cycles_counter zurueckgesetzt
			if(timer0Neu != timer0Alt){
				//Cycles Counter Reset
				Timer0.cycles_counter = 0;
				//Timer0 aus Speicher lesen
				Timer0.speicherInTimer();
			}
		}
		if(!OptionRegister.getT0CS()){
			//Cycles Counter erhöhen
			Timer0.cycles_counter += (taktzyklenNeu - taktzyklenAlt);
		}
		
		//Programcounter zusammensetzen oder PCL aktualisiseren
		Programcounter.pcZusammensetzen();
	}
	
	//Startet die Interrupt-Routine
	private static void interruptStarten(){
		//Ruecksprungadresse sichern
		Stack.writeStack();
		//GIE Bit clear
		Intcon.setGIE(false);
		
		cyclesAlt = SingleLayoutController.taktzyklen;
		Programcounter.pc = 4;
		
		//IN RETFI ausgefuehrt:
			//GIE Bit wieder setzen
			//Ruecksprungadresse holen
			//taktzyklen um insgesamt 2 erhoehen. Deshalb den alten Wert gespeichert
	}
}
