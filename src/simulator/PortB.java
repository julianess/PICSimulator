package simulator;

//Diese Klasse implementiert PortB des Microprozessors
//- Umwandlung des PortB Arrays in einen Integer Wert
//- PortB nach der Integer-Umwandlung an Adresse 0x6 schreiben 
//- Umwandlung des Integer Wertes aus Adresse 0x6 in das PortB Array
//- Back-End Logik beim Klicken auf die Port Bits (setzen/loeschen)
//- //Interrupt Bit RB0 extra abrufen

public class PortB {
	//PortB Array
	public static boolean [] portB = new boolean[8];
	
	//Umwandlung des PortB Arrays in einen Integer Wert
	public static short portBToInt(){
		short result = 0;
		
		for (int i = 0; i < portB.length; i++) {
			if (portB[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	//PortB nach der Integer-Umwandlung an Adresse 0x6 schreiben 
	public static void portBinSpeicher(){
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTB] = portBToInt();
	}
	
	//Umwandlung des Integer Wertes aus Adresse 0x6 in das PortB Array
	public static void setPortB(){
		for (int i = 0; i < portB.length; i++){
			if((BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTB] & (short) Math.pow(2, i)) != 0){
				portB[i] = true;
			}
			else{
				portB[i] = false;
			}
		}
	}
	
	//Back-End Logik beim Klicken auf die Port Bits (setzen/loeschen)
	public static void setPin(int pin){
		if (portB[pin]) {
			portB[pin] = false;
		}
		else {
			portB[pin] = true;	
		}
		
		portBinSpeicher();
		
	}
	
	//Interrupt Bit RB0 extra abrufen
	public static boolean getRB0INT(){
		return portB[0];
	}
}
