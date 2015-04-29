package simulator;


public class PortB {
	public static boolean [] portB = new boolean[8];
	
	public static short portBToInt(){
		short result = 0;
		
		for (int i = 0; i < portB.length; i++) {
			if (portB[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	public static void setPortB(){
		for (int i = 0; i < portB.length; i++){
			if((BefehlDecoder.speicherZellen[6] & (short) Math.pow(2, i)) != 0){
				portB[i] = true;
			}
			else{
				portB[i] = false;
			}
		}
	}
	
	public static void setPin(int pin){
		if (portB[pin]) {
			portB[pin] = false;
		}
		else {
			portB[pin] = true;	
		}
		
		BefehlDecoder.speicherZellen[0x6] = portBToInt();
		
	}
}
