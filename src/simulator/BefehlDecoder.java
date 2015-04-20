package simulator;

public class BefehlDecoder
{	
	//Konstanten zur besseren Lesbarkeit im Code
	private static short Z = 2;
	private static short RP0  = 5;
	private static short FSR1 = 4;
	private static short FRS2 =  132;
	private static short STATUS1 = 3;
	private static short STATUS2 =  131;
	private static short PCL1 = 2;
	private static short PCL2 = 130;
	
	private short befehlcode;
	private short wRegister;
	private short[] statusRegister = new short[8];
	private short[] speicherZellen = new short[256];
	
	public BefehlDecoder (){} //leerer Konstruktor

	public short getBefehlcode() {
		return befehlcode;
	}

	public void setBefehlcode(short befehlcode) {
		this.befehlcode = befehlcode;
	}
	
	public void decode(short befehlcode)
	{
		short befehl = 0;
		
		if(befehlcode >= 15360)
		{
			befehl = (short) (befehlcode & 15872);
			if (befehl == 15872)
				addlw(befehlcode);
			else if(befehlcode == 15360)
				sublw(befehlcode);
		}
		else if(befehlcode >= 14336)
		{
			befehl = (short) (befehlcode & 16128);
			if(befehl == 14848)
				xorlw(befehlcode);
			else if (befehl == 14592)
				andlw(befehlcode);
			else if (befehl == 14336)
				iorlw(befehlcode);
		}
		else if(befehlcode >= 12288)
		{
			befehl = (short) (befehlcode & 15360);
			if(befehl == 13312)
				retlw(befehlcode);
			else if(befehl == 12288)
				movlw(befehlcode);
		}
		else if(befehlcode >= 8192)
		{
			befehl = (short) (befehlcode & 14336);
			if(befehl == 10240)
				gotoBefehl(befehlcode);
			else if(befehl == 8192)
				call(befehlcode);
		}
		else if(befehlcode >= 4096)
		{
			befehl = (short) (befehlcode & 15360);
			if(befehl == 7168)
				btfss(befehlcode);
			else if(befehl == 6144)
				btfsc(befehlcode);
			else if(befehlcode == 5120)
				bsf(befehlcode);
			else if(befehlcode == 4096)
				bcf(befehlcode);
		}
		else if(befehlcode >= 512)
		{
			befehl = (short) (befehlcode & 16128);
			if(befehl == 3840)
				incfsz(befehlcode);
			else if(befehl == 3584)
				swapf(befehlcode);
			else if(befehl == 3328)
				rlf(befehlcode);
			else if(befehl == 3072)
				rrf(befehlcode);
			else if(befehl == 2816)
				decfsz(befehlcode);
			else if(befehl == 2560)
				incf(befehlcode);
			else if(befehl == 2304)
				comf(befehlcode);
			else if(befehl == 2048)
				movf(befehlcode);
			else if(befehl == 1792)
				addwf(befehlcode);
			else if(befehl == 1536)
				xorwf(befehlcode);
			else if(befehl == 1280)
				andwf(befehlcode);
			else if(befehl == 1024)
				iorwf(befehlcode);
			else if(befehl == 768)
				decf(befehlcode);
			else if(befehl == 512)
				subwf(befehlcode);
		}
		else if(befehlcode >= 256)
		{
			befehl = (short) (befehlcode & 16256);
			if(befehl == 384)
				clrf(befehlcode);
			else if(befehl == 256)
				clrw(befehlcode);
		}
		else if(befehlcode >= 128)
		{
			//Kein befehl verunden, da eindeutig
			movwf(befehlcode);
		}
		else if(befehlcode == 100)
			clrwdt();
		else if(befehlcode == 99)
			sleep();
		else if(befehlcode == 9)
			retfie();
		else if(befehlcode == 8)
			returnBefehl();
		else
			nop();
	}

	private void nop() {
		// TODO Auto-generated method stub
		
	}

	private void returnBefehl() {
		// TODO Auto-generated method stub
		
	}

	private void retfie() {
		// TODO Auto-generated method stub
		
	}

	private void sleep() {
		// TODO Auto-generated method stub
		
	}

	private void clrwdt() {
		// TODO Auto-generated method stub
		
	}

	private void movwf(short befehlcode2) {
		short argument = (short) (befehlcode2 & 127);
		
		//Prüfen, ob Bank 1 oder Bank 0; Bank 1: 127 dazu addieren (0x80)
		if (statusRegister[5] == 1) {
			argument += 127;
		}
		speicherZellen[argument] = wRegister;
	}

	private void clrw(short befehlcode2) {
		wRegister = 0;
		statusRegister[2] = 1;
		copyStatus();
		
	}

	private void clrf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void subwf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void decf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void iorwf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void andwf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void xorwf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void addwf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void movf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void comf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void incf(short befehlcode2) {
		if (speicherZellen[(befehlcode2 & 127)] >= 254) {
			statusRegister[Z] = 1;
		}
		if ((befehlcode2 & 128) == 0) {
			if (statusRegister[RP0] == 1) {
				wRegister = (short) (speicherZellen[(befehlcode2 & 127) + 127] += 1);
			}
			wRegister = (short) (speicherZellen[befehlcode2 & 127] += 1);
		}
		else {
			if (statusRegister[RP0]  == 1) {
				speicherZellen[(befehlcode2 & 127) + 127] += 1;
			}
			speicherZellen[befehlcode2 & 127] += 1;
		}
	}

	private void decfsz(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void rrf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void rlf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void swapf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void incfsz(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void bcf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void bsf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void btfsc(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void btfss(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void call(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void gotoBefehl(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void movlw(short befehlcode2) {
		wRegister = (short) (befehlcode2 & 255);
	}

	private void retlw(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void iorlw(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void andlw(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void xorlw(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void sublw(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void addlw(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}
	
	private void copyStatus(){
		//ByteArray in eine zahl konvertieren
		short zahl = 0;
		for (int i = 0; i < 8; i++) {
			zahl += (short) (statusRegister[i] * Math.pow(2, (8-i)));
		}
		//Status Register in 0x03 und 0x83 speichern
		speicherZellen[3] = speicherZellen[131] = zahl;
	}
	
}
