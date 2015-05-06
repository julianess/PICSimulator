package simulator;

public class TrisB {
	public static boolean [] trisB = new boolean[8];
	
	public static short trisBToInt(){
		short result = 0;
		
		for (int i = 0; i < trisB.length; i++) {
			if (trisB[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
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
