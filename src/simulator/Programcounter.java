package simulator;

//Diese Klasse implementiert den Programmcounter
// - PCL
// - PCLATH
// - Verhalten bei Call und Goto

public class Programcounter {
	public static int pc = 0;
	public static short pcl = 0;
	public static short pclath = 0;
	
	public static void pcZusammensetzen(){
		//5 Bit PCLATH relevant
		pclath = (short) (pclath & 31);
		//8 Bit PCL relevant
		pcl = (short) (pcl & 255);
		
		//Nur zusammensetzen, wenn in PCL Register geschrieben wurd
		if(Interrupt.pclNeu != Interrupt.pclAlt){
			//PC setzt sich zusammen aus PCLATH (Bit 12-8) und PCL (Bit 7 - 0)
			//Dabei sind nur Bit3 und Bit 4 des PCLATH relevant, daher die Verundung
			pc = (int) (pclath * Math.pow(2, 8)) + pcl;
		}
		else{
			//PCL = Bits 0-7 des PC
			pcl = (short) (pc & 255);
			
			//PCL an seine Speicheradressen schreiben
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_0] = pcl;
			BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PCL_1] = pcl;
		}
	}
	
	public static void pcCallGoto(short k){
		//k auf Bits 0-10, pclath Bits 3-4 auf Bits 11-12
		pc = k + ((pclath & 24) << 8);
	}
}
