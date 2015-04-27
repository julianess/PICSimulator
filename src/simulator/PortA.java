package simulator;

public class PortA {
	public static boolean [] portA = new boolean[8];
	
	public static short portAToInt(){
		short result = 0;
		
		for (int i = 0; i < portA.length; i++) {
			if (portA[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	public static void setPortA(){
		short test = BefehlDecoder.speicherZellen[3];
		for (int i = 0; i < portA.length; i++){
			if((test & (short) Math.pow(2, i)) != 0){
				portA[i] = true;
			}
			else{
				portA[i] = false;
			}
		}
	}
	
	public static void setPin(int pin){
		if (portA[pin]) {
			portA[pin] = false;
		}
		else {
			portA[pin] = true;	
		}
		
		BefehlDecoder.speicherZellen[0x5] = portAToInt();
	}
	
}
