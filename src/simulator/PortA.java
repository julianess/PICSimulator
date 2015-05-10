package simulator;

//Diese Klasse implementiert PortA des Microprozessors
//- Umwandlung des PortA Arrays in einen Integer Wert
//- Umwandlung des Integer Wertes aus Adresse 0x5 in das PortA Array
//- Back-End Logik beim Klicken auf die Port Bits (setzen/loeschen)

public class PortA {
	//PortA Array
	public static boolean [] portA = new boolean[8];
	
	//Umwandlung des PortA Arrays in einen Integer Wert
	public static short portAToInt(){
		short result = 0;
		
		for (int i = 0; i < portA.length; i++) {
			if (portA[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	//Umwandlung des Integer Wertes aus Adresse 0x5 in das PortA Array
	public static void setPortA(){
		for (int i = 0; i < portA.length; i++){
			if((BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTA] & (short) Math.pow(2, i)) != 0){
				portA[i] = true;
			}
			else{
				portA[i] = false;
			}
		}
	}
	
	//Back-End Logik beim Klicken auf die Port Bits (setzen/loeschen)
	public static void setPin(int pin){
		if (portA[pin]) {
			portA[pin] = false;
		}
		else {
			portA[pin] = true;	
		}
		
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTA] = portAToInt();
	}
	
}
