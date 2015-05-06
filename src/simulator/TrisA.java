package simulator;

public class TrisA {
	public static boolean [] trisA = new boolean[8];
	
	public static short trisAToInt(){
		short result = 0;
		
		for (int i = 0; i < trisA.length; i++) {
			if (trisA[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
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
