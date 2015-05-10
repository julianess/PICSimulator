package simulator;

//Diese Klasse implementiert das Status Register

public class StatusRegister {
	private static final short C 	= 0; //Carry
	private static final short DC 	= 1; //Digit Carry
	private static final short Z 	= 2; //Zero-Flag
	private static final short PD 	= 3; //Power-down
	private static final short TO 	= 4; //Time-out
	private static final short RP0  = 5; //Register Bank select
	
	//Array fuer Register
	private static boolean[] register = new boolean[8];
	
	//TODO: In RESET outsourcen!!!!! Bzg. RESET aufrufen	
	{register[RP0] = false;}
	public StatusRegister(){
		register[6] = false;
		register[7] = false;
	}
	////
	
	//Z-Flag setzen
	public static void setZFlag(boolean status){
		register[Z] = status;
		statusInSpeicher(); //Status Register Array in die Speichertabelle schreiben
	}
	
	//Z-Flag abfragen
	public static boolean getZFlag(){
		return register[Z];
	}
	
	//RP0 setzen: true = bank1 , false = bank0
	public static void setBank(boolean status){
		register[RP0] = status;
		statusInSpeicher(); //Status Register Array in die Speichertabelle schreiben
	}
	
	//RP0 abfragen: true = bank1 , false = bank0
	public static boolean getBank(){
		return register[RP0];
	}
	
	//Carry Bit setzen bzw loeschen
	public static void setCarryBit(boolean status){
		register[C] = status;
		statusInSpeicher(); //Status Register Array in die Speichertabelle schreiben
	}
	
	//Carry Bit abfragen
	public static boolean getCarryBit(){
		return register[C];
	}
	
	//Digit Carry Bit setzen bzw loeschen
	public static void setDigitCarryBit(boolean status){
		register[DC] = status;
		statusInSpeicher(); //Status Register Array in die Speichertabelle schreiben
	}
	
	//Digit Carry Bit abfragen
	public static boolean getDigitCarryBit(){
		return register[DC];
	}
	
	//Wert des Status Register Arrays in einen Integer-Wert umwandeln
	public static short registerToInt(){
		short result = 0;
		
		for (int i = 0; i < register.length; i++) {
			if (register[i]) {
				result += Math.pow(2, (i));
			}
		}
		return result;
	}
	
	//Wert des Status Register Arrays in die Speichertabelle schreiben
	public static void statusInSpeicher(){
		//Status Register in 0x03 und 0x83 speichern
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_0] = registerToInt();
		BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_1] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_0];
	}
	
	//Status Register Array aus dem integer-Wert aus der Speichertabelle bilden
	public static void speicherInStatus(){
		short wert = 0;
		if(getBank()){
			wert = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_1];
		}
		else{
			wert = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_0];
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
