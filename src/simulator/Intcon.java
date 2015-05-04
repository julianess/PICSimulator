package simulator;

public class Intcon {
	private static final short RBIF 	= 0; //RB Port Change Interrupt Flag bit
	private static final short INTF 	= 1; //RB0/INT External Interrupt Flag bit
	private static final short T0IF		= 2; //TMR0 Overflow Interrupt Flag bit
	private static final short RBIE		= 3; //RB Port Change Interrupt Enable bit
	private static final short INTE		= 4; //RB0/INT External Interrupt Enable bit
	private static final short T0IE		= 5; //TMR0 Overflow Interrupt Enable bit
	private static final short EEIE		= 6; //EE Write Complete Interrupt Enable bit
	private static final short GIE		= 7; //Global Interrupt Enable bit
	
	private static short intconadr = 0;
	public static boolean [] intcon = new boolean[8];
	
	
	//RBIE setzen
	public static void setRBIE(boolean status){
		intcon[RBIE] = status;
		intconInSpeicher(); //Intcon Register Array in die Speichertabelle schreiben
	}
	
	//RBIE abfragen
	public static boolean getRBIE(){
		return intcon[RBIE];
	}
	
	//RBIF setzen
	public static void setRBIF(boolean status){
		intcon[RBIF] = status;
		intconInSpeicher(); //Intcon Register Array in die Speichertabelle schreiben
	}
	
	//RBIF abfragen
	public static boolean getRBIF(){
		return intcon[RBIF];
	}
	
	//T0IE setzen
	public static void setT0IE(boolean status){
		intcon[T0IE] = status;
		intconInSpeicher(); //Intcon Register Array in die Speichertabelle schreiben
	}
	
	//T0IE abfragen
	public static boolean getT0IE(){
		return intcon[T0IE];
	}
	
	//T0IF setzen
	public static void setT0IF(boolean status){
		intcon[T0IF] = status;
		intconInSpeicher(); //Intcon Register Array in die Speichertabelle schreiben
	}
	
	//T0IF abfragen
	public static boolean getT0IF(){
		return intcon[T0IF];
	}
	
	//INTF setzen
	public static void setINTF(boolean status){
		intcon[INTF] = status;
		intconInSpeicher(); //Intcon Register Array in die Speichertabelle schreiben
	}
	
	//INTF abfragen
	public static boolean getINTF(){
		return intcon[INTF];
	}
	
	//INTE setzen
	public static void setINTE(boolean status){
		intcon[INTE] = status;
		intconInSpeicher(); //Intcon Register Array in die Speichertabelle schreiben
	}
	
	//INTF abfragen
	public static boolean getINTE(){
		return intcon[INTE];
	}
	
	//GIE setzen
	public static void setGIE(boolean status){
		intcon[GIE] = status;
		intconInSpeicher(); //Intcon Register Array in die Speichertabelle schreiben
	}
	
	//GIE abfragen
	public static boolean getGIE(){
		return intcon[GIE];
	}
		
	
	
	//Intcon Array in Speicher schreiben
	public static short intconToInt(){
		short result = 0;
		
		for (int i = 0; i < intcon.length; i++) {
			if (intcon[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	//Wert des Intcon Register Arrays in die Speichertabelle schreiben
	public static void intconInSpeicher(){
		//Status Register in 0x0B und 0x8B speichern
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_0] = intconToInt();
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_1] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_0];
	}
	
	//Intcon Register Array aus dem integer-Wert aus der Speichertabelle bilden
	public static void speicherInIntcon(){
		if(StatusRegister.getBank()){
			intconadr = RegisterAdressen.ADR_INTCON_1;
		}
		else{
			intconadr = RegisterAdressen.ADR_INTCON_0;
		}
		
		for (int i = 0; i < intcon.length; i++){
			if((BefehlDecoder.speicherZellen[intconadr] & (short) Math.pow(2, i)) != 0){
				intcon[i] = true;
			}
			else{
				intcon[i] = false;
			}
		}
	}
}
