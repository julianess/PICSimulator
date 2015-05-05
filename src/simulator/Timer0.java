package simulator;


public class Timer0 {
	static short timer0 = 0;
	static short prescaler = 0;
	static short cycles_counter = 0;
	
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
			if(OptionRegister.getPS1()){
				prescaler += 2;
			}
			if(OptionRegister.getPS0()){
				prescaler += 1;
			}
			prescaler = (short) Math.pow(2, (prescaler+1));
		}
		else{
			prescaler = 1;
		}
			
		System.out.println("Prescaler: " + prescaler);
		//Timer 0 erhoehen, wenn Prescaler erreicht
		//Counter muss nicht exakt Prescaler sein (doppelte Cyclen)
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
}
