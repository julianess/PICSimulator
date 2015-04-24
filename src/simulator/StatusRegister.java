package simulator;

import simulator.controller.SingleLayoutController;

public class StatusRegister {
	private static final short Z = 2;
	private static final short BANK  = 5;
	private static final short FSR1 = 4;
	private static final short FRS2 =  132;
	private static final short STATUS1 = 3;
	private static final short STATUS2 =  131;
	private static final short PCL1 = 2;
	private static final short PCL2 = 130;
	private static final short C = 0;
	private static final short DC = 1;
	
	public static StatusRegister instance = null;
	private static boolean[] register = new boolean[8];
	
	
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
	}
	
	public static boolean statusZFlag(){
		return register[Z];
	}
	
	public static void setBank(boolean status){
		register[BANK] = status;
	}
	
	public static boolean statusBank(){
		return register[BANK];
	}
	
	public static void setCarryBit(boolean status){
		register[C] = status;
	}
	
	public static boolean statusCarryBit(){
		return register[C];
	}
	
	public static void setDigitCarryBit(boolean status){
		register[DC] = status;
	}
	
	public static boolean statusDigitCarryBit(){
		return register[DC];
	}
	
	public static short registerToInt(){
		short result = 0;
		
		for (int i = 0; i < register.length; i++) {
			if (register[i]) {
				result += Math.pow(2, (8-i));
			}
		}
		return result;
	}
	
}
