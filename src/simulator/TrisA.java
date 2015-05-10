package simulator;

//Diese Klasse implementiert das TrisA Register (PortA Pins als Input oder Output festlegen)
// - trisA Array in einen Integer Wert umwandeln
// - Integer Wert von Adresse 0x85 in trisA Array umwandeln


public class TrisA {
	public static boolean [] trisA = new boolean[8]; //trisA Array
	
	//trisA Array in einen Integer Wert umwandeln
	public static short trisAToInt(){
		short result = 0;
		
		for (int i = 0; i < trisA.length; i++) {
			if (trisA[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	//Integer Wert von Adresse 0x85 in trisA Array umwandeln
	public static void speicherInTrisA(){
		for (int i = 0; i < trisA.length; i++){
			if((BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TRISA] & (short) Math.pow(2, i)) != 0){
				trisA[i] = true;
			}
			else{
				trisA[i] = false;
			}
		}
	}
}
