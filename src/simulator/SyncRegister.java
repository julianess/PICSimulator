package simulator;

public class SyncRegister {
	private static final short FSR0 = 4;
	private static final short FSR1 =  132;
	private static final short PCL0 = 2;
	private static final short PCL1 = 130;
	private static final short INTCON0 = 11;
	private static final short INTCON1 = 139;
	
	public static void synchronisieren(){
	
		if(StatusRegister.statusBank()){
			BefehlDecoder.speicherZellen[FSR0] = BefehlDecoder.speicherZellen[FSR1];
			BefehlDecoder.speicherZellen[PCL0] = BefehlDecoder.speicherZellen[PCL1];
			BefehlDecoder.speicherZellen[INTCON0] = BefehlDecoder.speicherZellen[INTCON1];
		}
		else{
			BefehlDecoder.speicherZellen[FSR1] = BefehlDecoder.speicherZellen[FSR0];
			BefehlDecoder.speicherZellen[PCL1] = BefehlDecoder.speicherZellen[PCL0];
			BefehlDecoder.speicherZellen[INTCON1] = BefehlDecoder.speicherZellen[INTCON0];
		}
	} 
}
