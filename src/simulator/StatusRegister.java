package simulator;

public class StatusRegister {
	private static final short Z = 2;
	private static final short BANK  = 5;
	private static final short C = 0;
	private static final short DC = 1;
	
	public static StatusRegister instance = null;
	private static boolean[] register = new boolean[8];
	
	{register[BANK] = false;}
	
	
	public StatusRegister(){
		register[6] = false;
		register[7] = false;
	}
	
	public StatusRegister getInstance(){
		if (instance == null) {
			instance = new StatusRegister();
		}
		return instance;
	}
	
	public static void setZFlag(boolean status){
		register[Z] = status;
		statusInSpeicher();
	}
	
	public static boolean statusZFlag(){
		return register[Z];
	}
	
	public static void setBank(boolean status){
		register[BANK] = status;
		statusInSpeicher();
	}
	
	public static boolean statusBank(){
		return register[BANK];
	}
	
	public static void setCarryBit(boolean status){
		register[C] = status;
		statusInSpeicher();
	}
	
	public static boolean statusCarryBit(){
		return register[C];
	}
	
	public static void setDigitCarryBit(boolean status){
		register[DC] = status;
		statusInSpeicher();
	}
	
	public static boolean statusDigitCarryBit(){
		return register[DC];
	}
	
	public static short registerToInt(){
		short result = 0;
		
		for (int i = 0; i < register.length; i++) {
			if (register[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	public static void statusInSpeicher(){
		//Status Register in 0x03 und 0x83 speichern
		BefehlDecoder.speicherZellen[3] = registerToInt();
		BefehlDecoder.speicherZellen[131] = BefehlDecoder.speicherZellen[3];
	}
	
	public static void speicherInStatus(){
		short wert = 0;
		if(statusBank()){
			wert = BefehlDecoder.speicherZellen[131];
		}
		else{
			wert = BefehlDecoder.speicherZellen[3];
		}
		
		for (int i = 0; i < register.length; i++){
			if((wert & (short) Math.pow(2, i)) != 0){
				register[i] = true;
			}
			else{
				register[i] = false;
			}
		}
	}
}
