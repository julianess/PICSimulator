package simulator;

import simulator.controller.SingleLayoutController;

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
	private static short C = 0;
	private static short DC = 1;
	
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
		
		//Pruefen, ob Bank 1 oder Bank 0; Bank 1: 127 dazu addieren (0x80)
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
		short d = (short) (befehlcode2 & 128);
		short f = (short) (befehlcode2 & 127);
		
		short k = (short) ((speicherZellen[f] ^ 255)+1); //Zweierkomplement -> Gleiche Methode wie addwf
		
		if((k & 15 + wRegister & 15) >= 16) {
			statusRegister[DC] = 1;
		}
		else {
			statusRegister[DC] = 0;
		}
		
		//mit Überlauf über 255
		if(wRegister + k > 255)
		{
			statusRegister[C] = 1; //Carry Bit setzen
			
			if(wRegister + k == 256) {
				statusRegister[Z] = 1;
			}
			else {
				statusRegister[Z] = 0;
			}
			
			//Nach Überlauf wieder bei 0 anfangen
			if(d == 0)
			{
				wRegister = (short) (wRegister + k - 256);
			}
			else
			{
				speicherZellen[f] = (short) (wRegister + k - 256);
			}
		}
		else {
			statusRegister[C] = 0; //Carry Bit leeren
			
			if(d == 0)
			{
				wRegister = (short) (wRegister + k);
			}
			else
			{
				speicherZellen[f] = (short) (wRegister + k);
			}
		}		
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
		
		short d = (short)(befehlcode2 & 128);
		short f = (short)(befehlcode2 & 127);
		
		if((speicherZellen[f] & 15 + wRegister & 15) >= 16) {
			statusRegister[DC] = 1;
		}
		else {
			statusRegister[DC] = 0;
		}
		
		//mit Überlauf über 255
		if(wRegister + speicherZellen[f] > 255)
		{
			statusRegister[C] = 1; //Carry Bit setzen
			
			if(wRegister + speicherZellen[f] == 256) {
				statusRegister[Z] = 1;
			}
			else {
				statusRegister[Z] = 0;
			}
			
			//Nach Überlauf wieder bei 0 anfangen
			if(d == 0)
			{
				wRegister = (short) (wRegister + speicherZellen[f] - 256);
			}
			else
			{
				speicherZellen[f] = (short) (wRegister + speicherZellen[f] - 256);
			}
		}
		else {
			statusRegister[C] = 0; //Carry Bit leeren
			
			if(d == 0)
			{
				wRegister = (short) (wRegister + speicherZellen[f]);
			}
			else
			{
				speicherZellen[f] = (short) (wRegister + speicherZellen[f]);
			}
		}			
	}

	private void movf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void comf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = (short)(befehlcode2 & 127);
		//temporaere Variable fuer das Ergebnis
		short tmp = (short) (speicherZellen[f] ^ 255);
		
		if(tmp == 0)
		{
			statusRegister[Z] = 1;
		}
		else
		{
			statusRegister[Z] = 0;
		}
				
		if(d == 0)
		{
			wRegister = tmp;
		}
		else
		{
			speicherZellen[f] = tmp;
		}	
	}

	private void incf(short befehlcode2) {
		//Adresse f maskieren auf 7 bit
		short f = (short) (befehlcode2 & 127);
		
		if (statusRegister[RP0] == 1){
			f += 128;	
		}
		
		if (speicherZellen[f] >= 254) {
			statusRegister[Z] = 1;
		}
		else{
			statusRegister[Z] = 0;
		}
		
		if ((befehlcode2 & 128) == 0) {
			if(statusRegister[Z] == 0) {
				wRegister = (short) (speicherZellen[f] += 1);
			}
			else {
				wRegister = 0;
			}
		}
		else {
			if(statusRegister[Z] == 0) {
				speicherZellen[f] += 1;
			}
			else {
				speicherZellen[f] = 0;
			}
		}
		copyStatus();
	}

	private void decfsz(short befehlcode2) {
		
		short d = (short) (befehlcode2 & 128);
		short f = (short) (befehlcode2 & 127);
		
		//Ergebnis in W-Register
		if(d == 0)
		{
			//Bank 1 oder Bank 0, wenn Bank 1, Adresse um 128 erhöhen
			if(statusRegister[RP0] == 1)
			{
				f += 128;
			}
			
			if(statusRegister[f] == 0)
			{
				wRegister = 255;
			}
			else
			{
				wRegister = (short) (statusRegister[f] - 1);
			}
		}
		//Ergebnis in f
		else
		{
			if(statusRegister[f] == 0)
			{
				statusRegister[f] = 255;
			}
			else
			{
				statusRegister[f] = (short) (statusRegister[f] - 1);
			}
		}
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
		short b = (short) (befehlcode2 & 896);
		short f = (short) (befehlcode2 & 127);
		
		//WEITER MACHEN!!!
		
	}

	private void btfsc(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void btfss(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void call(short befehlcode2) {
		short k = (short) (befehlcode2 & 255);
		SingleLayoutController.writeCounter();
		SingleLayoutController.programcounter = k;	
	}

	private void gotoBefehl(short befehlcode2) {

		//Richtigen PC evtl noch implementieren
		short k = (short) (befehlcode2 & 127);
		SingleLayoutController.programcounter = k;
	}

	private void movlw(short befehlcode2) {
		wRegister = (short) (befehlcode2 & 255);
	}

	private void retlw(short befehlcode2) {
		short k = (short)(befehlcode2 & 255);
		wRegister = k;
		SingleLayoutController.getCounter();	
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
		
		//Literal l maskieren
		short l = (short) (befehlcode2 & 255);
		
		//mit Überlauf der hinteren 4 bit
		if((l & 15 + wRegister & 15) >= 16) {
			statusRegister[DC] = 1;
		}
		else {
			statusRegister[DC] = 0;
		}
		
		//mit Überlauf über 255
		if(wRegister + l > 255)
		{
			statusRegister[C] = 1; //Carry Bit setzen
			if(wRegister + l == 256) {
				statusRegister[Z] = 1;
			}
			else {
				statusRegister[Z] = 0;
			}
			
			//Nach Überlauf wieder bei 0 anfangen
			wRegister = (short) (wRegister + l - 256);
		}
		else {
			statusRegister[C] = 0; //Carry Bit leeren
			wRegister = (short) (wRegister + l);
		}		
	}
	
	private void copyStatus(){
		//ByteArray in eine Zahl konvertieren
		short zahl = 0;
		for (int i = 0; i < 8; i++) {
			zahl += (short) (statusRegister[i] * Math.pow(2, (8-i)));
		}
		//Status Register in 0x03 und 0x83 speichern
		speicherZellen[3] = speicherZellen[131] = zahl;
	}
	
}
