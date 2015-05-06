package simulator;

//Klasse fuer die Synchronisierung der "doppelt vorhandenen" Register
public class SyncRegister {
	
	//Register zwischen Bank 0 und Bank 1 synchronisieren
	public static void synchronisieren(){
	
		//Wenn Bank 1
		if(StatusRegister.getBank()){
			//Status Register
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_0] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_1];
			//FSR
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_FSR_0] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_FSR_1];
			//PCL
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_0] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_1];
			//INTCON
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_0] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_1];
			//PCLATH
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCLATH_0] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCLATH_1];
		}
		//Wenn Bank 0
		else{
			//Status Register
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_1] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_STATUSREGISTER_0];
			//FSR
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_FSR_1] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_FSR_0];
			//PCL
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_1] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_0];
			//INTCON
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_1] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_INTCON_0];
			//PCLATH
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCLATH_1] = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCLATH_0];
		}
	}
	
	public static void speicherInRegister(){
		//OptionRegister Array aus Speicher laden
		OptionRegister.speicherInOption();
		//Timer0 aus Speicher laden
		Timer0.speicherInTimer();
		//TrisA Array aus Speicher laden
		TrisA.speicherInTrisA();
		//TrisB Array aus Speicher laden
		TrisB.speicherInTrisB();
	}
}
