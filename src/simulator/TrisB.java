package simulator;

//Diese Klasse implementiert das TrisB Register (PortB Pins als Input oder Output festlegen)
//- trisB Array in einen Integer Wert umwandeln
//- Integer Wert von Adresse 0x86 in trisB Array umwandeln

public class TrisB {
	public static boolean [] trisB = new boolean[8]; //trisB Array
	
	//trisB Array in einen Integer Wert umwandeln
	public static short trisBToInt(){
		short result = 0;
		
		for (int i = 0; i < trisB.length; i++) {
			if (trisB[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	//Integer Wert von Adresse 0x86 in trisB Array umwandeln
	public static void speicherInTrisB(){
		for (int i = 0; i < trisB.length; i++){
			if((BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TRISB] & (short) Math.pow(2, i)) != 0){
				trisB[i] = true;
			}
			else{
				trisB[i] = false;
			}
		}
	}
}
