package simulator;

import simulator.controller.SingleLayoutController;

public class Timer0 {
	static short timer0 = 0;
	static short prescaler = 0;
	
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
			
			//Timer0 erhoehen, wenn Prescaler erreicht
			if((SingleLayoutController.taktzyklen % prescaler) == 0){
				if(timer0 == 255){
					timer0 = 0;
					//INTERRUPT AUFRUFEN!!!!!!!!!!!!!!!!!!
				}
				else{
					timer0 ++;
				}
			}
		}
	}
	
	//Wert des Timers in seinen Speicher schreiben
	public static void timerInSpeicher(){
		//Timer0 in 0x1 speichern
		BefehlDecoder.speicherZellen[1] = timer0;
	}
	
	//Wert des Timers aus seinem Speicher lesen
	public static void speicherInTimer(){
		//Timer aus 0x1 holen
		timer0 = BefehlDecoder.speicherZellen[1];
	}
}
