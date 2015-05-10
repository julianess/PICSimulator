package simulator;

//Diese Klasse implementiert den Timer0
// - Timer0 berechnen (intern/extern)
// - Prescaler
// - Timer0 aus seiner Speicheradresse laden
// - Timer0 an seine Speicheradresse schreiben

public class Timer0 {
	static short timer0 = 0; //Wert des Timer0
	static short prescaler = 0; //Wert des Prescalers
	static short cycles_counter = 0; //Zaehler der relevanten Prescaler Cycles
	
	//RA4 Variablen fuer den Vergleich (Timer0 abhaengig von RA4-Flanken)
	private static boolean portRA_4Alt = false;
	private static boolean portRA_4Neu = false;
	
	public static void berechneTimer0(){
		
		//Pruefen, ob prescaler sich auf Timer0 bezieht und wenn ja, die Berechnung durchfuehren
		if(!OptionRegister.getPSA()){
			//PS2
			if(OptionRegister.getPS2()){
				prescaler = 4;
			}
			else{
				prescaler = 0;
			}
			//PS1
			if(OptionRegister.getPS1()){
				prescaler += 2;
			}
			//PS0
			if(OptionRegister.getPS0()){
				prescaler += 1;
			}
			prescaler = (short) Math.pow(2, (prescaler+1));
		}
		//PSA gesetzt -> Prescaler gehoert zum Watchdog -> Kein Prescaler fuer Timer0
		else{
			prescaler = 1;
		}
		
		
		//Clock Source: Instrucition Cyrcles
		if(!OptionRegister.getT0CS()){
			//Timer 0 erhoehen, wenn Prescaler erreicht
			//Counter muss nicht exakt Prescaler sein (doppelte Zyclen)
			if(cycles_counter >= prescaler){
				if(timer0 >= 255){
					timer0 = (short) (timer0 - 255);
					Intcon.setT0IF(true);
				}
				else{
					timer0 ++;
				}
				//Counter zuruecksetzen
				cycles_counter -= prescaler;
				//Timer0 an seine Speicheradresse schreiben
				timerInSpeicher();
			}
		}
		
		//Externer Takt RA4
		else{
			portRA_4Neu = getRA4();
			//Steigende Flanke: T0SE nicht gesetzt
			if(portRA_4Neu && !portRA_4Alt && !OptionRegister.getT0SE()){
				//Cycles Counter erhoehen
				cycles_counter ++;
				//Timer 0 erhoehen, wenn Prescaler erreicht
				//Counter muss nicht exakt Prescaler sein (doppelte Zyclen)
				if(cycles_counter >= prescaler){
					if(timer0 >= 255){
						//timer0 zuruecksetzen
						timer0 = (short) (timer0 - 255);
						//Timer0 Interrupt Flag setzen
						Intcon.setT0IF(true);
					}
					else{
						//timer0 erhoehen
						timer0 ++;
					}
					//Counter zuruecksetzen
					cycles_counter -= prescaler;
					//Timer0 an seine Speicheradresse schreiben
					timerInSpeicher();
				}
			}
			//fallende Flanke
			else if(!portRA_4Neu && portRA_4Alt && OptionRegister.getT0SE()){
				//Cycles Counter erhoehen
				cycles_counter ++;
				if(cycles_counter >= prescaler){
					if(timer0 >= 255){
						//timer0 zuruecksetzen
						timer0 = (short) (timer0 - 255);
						//Timer0 Interrupt Flag setzen
						Intcon.setT0IF(true);
					}
					else{
						//timer0 erhoehen
						timer0 ++;
					}
					//Counter zuruecksetzen
					cycles_counter -= prescaler;
					//Timer0 an seine Speicheradresse schreiben
					timerInSpeicher();
				}
			}
			//Alten Wert aktualisieren
			portRA_4Alt = portRA_4Neu;
		}
	}
	
	//Wert des Timers in seinen Speicher schreiben
	public static void timerInSpeicher(){
		//Timer0 in 0x1 speichern
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TMR0] = timer0;
	}
	
	//Wert des Timers aus seinem Speicher lesen
	public static void speicherInTimer(){
		//Timer aus 0x1 holen
		timer0 = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TMR0];
	}
	
	public static boolean getRA4(){
		if((BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTA] & 16) != 0){
			return true;
		}
		else{
			return false;
		}
		
	}
}
