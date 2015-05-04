package simulator;

public class OptionRegister {
	private static final short PS0 = 0;
	private static final short PS1 = 1;
	private static final short PS2 = 2;
	private static final short PSA = 3;
	private static final short T0SE = 4;
	private static final short T0CS = 5;
	private static final short INTEDG = 6;
	private static final short RBPU = 7;
	
	public static OptionRegister instance = null;
	private static boolean[] register = new boolean[8];
	
	//Wert des Registers als short zurueckgeben
	public static short registerToInt(){
		short result = 0;
		
		for (int i = 0; i < register.length; i++) {
			if (register[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	//Wert des Registers in seinen Speicher schreiben
	public static void optionInSpeicher(){
		//Option Register in 0x81 speichern
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_OPTIONREG] = registerToInt();
	}
	
	//Speicher in Option Register Array schreiben
	public static void speicherInOption(){
		short wert = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_OPTIONREG];
		
		for (int i = 0; i < register.length; i++){
			if((wert & (short) Math.pow(2, i)) != 0){
				register[i] = true;
			}
			else{
				register[i] = false;
			}
		}
	}
	
	//Getter und Setter der Register Bits
	//PS0
	public static void setPS0(boolean status){
		register[PS0] = status;
		optionInSpeicher();
	}
	
	public static boolean getPS0(){
		return register[PS0];
	}
	
	//PS1
	public static void setPS1(boolean status){
		register[PS1] = status;
		optionInSpeicher();
	}
	
	public static boolean getPS1(){
		return register[PS1];
	}
	
	//PS2
	public static void setPS2(boolean status){
		register[PS2] = status;
		optionInSpeicher();
	}
	
	public static boolean getPS2(){
		return register[PS2];
	}
	
	//PSA
	public static void setPSA(boolean status){
		register[PSA] = status;
		optionInSpeicher();
	}
	
	public static boolean getPSA(){
		return register[PSA];
	}
	
	//T0SE
	public static void setT0SE(boolean status){
		register[T0SE] = status;
		optionInSpeicher();
	}
	
	public static boolean getT0SE(){
		return register[T0SE];
	}
	
	//T0CS
	public static void setT0CS(boolean status){
		register[T0CS] = status;
		optionInSpeicher();
	}
	
	public static boolean getT0CS(){
		return register[T0CS];
	}
	
	//INTEDG
	public static void setINTEDG(boolean status){
		register[INTEDG] = status;
		optionInSpeicher();
	}
	
	public static boolean getINTEDG(){
		return register[INTEDG];
	}
	
	//RBPU
	public static void setRBPU(boolean status){
		register[RBPU] = status;
		optionInSpeicher();
	}
	
	public static boolean getRBPU(){
		return register[RBPU];
	}
}
