package simulator;

import simulator.controller.SingleLayoutController;

public class BefehlDecoder
{	
	private static final short FSR = 4;
	private short befehlcode;
	public static short wRegister;
	public static short[] speicherZellen = new short[256];
	
	public BefehlDecoder (){} //leerer Konstruktor

	public short getBefehlcode() {
		return befehlcode;
	}

	public void setBefehlcode(short befehlcode) {
		this.befehlcode = befehlcode;
	}
	
	public void decode(short befehlcode)
	{
		System.out.println("ruft auf mit " + befehlcode);
		short befehl = 0;
		
		if(befehlcode >= 15360)
		{
			befehl = (short) (befehlcode & 15872);
			if (befehl == 15872)
				addlw(befehlcode);
			else if(befehl == 15360)
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
			else if(befehl == 5120)
				bsf(befehlcode);
			else if(befehl == 4096)
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
		SingleLayoutController.programcounter ++;
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
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		//Pruefen, ob Bank 1 oder Bank 0; Bank 1: 127 dazu addieren (0x80)
		if (StatusRegister.statusBank()) {
			f += 127;
		}
		speicherZellen[f] = wRegister;
		
		SingleLayoutController.programcounter ++;
		StatusRegister.setStatus();
	}

	private void clrw(short befehlcode2) {
		wRegister = 0;
		StatusRegister.setZFlag(true);
		StatusRegister.copyStatus();
		
		SingleLayoutController.programcounter ++;
	}

	private void clrf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void subwf(short befehlcode2) {
		short d = (short) (befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		short k = (short) ((speicherZellen[f] ^ 255)+1); //Zweierkomplement -> Gleiche Methode wie addwf
		
		if((k & 15 + wRegister & 15) >= 16) {
			StatusRegister.setDigitCarryBit(true);
		}
		else {
			StatusRegister.setCarryBit(false);
		}
		
		//mit Ueberlauf Ueber 255
		if(wRegister + k > 255)
		{
			//Carry Bit setzen
			StatusRegister.setCarryBit(true);
			
			if(wRegister + k == 256) {
				StatusRegister.setZFlag(true);
			}
			else {
				StatusRegister.setZFlag(false);
			}
			
			//Nach �berlauf wieder bei 0 anfangen
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
			StatusRegister.setCarryBit(false); //Carry Bit leeren
			
			if(d == 0)
			{
				wRegister = (short) (wRegister + k);
			}
			else
			{
				speicherZellen[f] = (short) (wRegister + k);
			}
		}
		
		SingleLayoutController.programcounter ++;
		StatusRegister.setStatus();
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
		short f = maskiereAdresse(befehlcode2); //(short)(befehlcode2 & 127);
		
		if((speicherZellen[f] & 15 + wRegister & 15) >= 16) {
			StatusRegister.setDigitCarryBit(true);
		}
		else {
			StatusRegister.setDigitCarryBit(false);
		}
		
		//mit Ueberlauf Ueber 255
		if(wRegister + speicherZellen[f] > 255)
		{
			StatusRegister.setCarryBit(true); //Carry Bit setzen
			
			if(wRegister + speicherZellen[f] == 256) {
				StatusRegister.setZFlag(true);
			}
			else {
				StatusRegister.setZFlag(false);
			}
			
			//Nach Ueberlauf wieder bei 0 anfangen
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
			//Carry Bit leeren
			StatusRegister.setCarryBit(false);
			
			if(d == 0)
			{
				wRegister = (short) (wRegister + speicherZellen[f]);
			}
			else
			{
				speicherZellen[f] = (short) (wRegister + speicherZellen[f]);
			}
		}
		
		SingleLayoutController.programcounter ++;
		StatusRegister.setStatus();
	}

	private void movf(short befehlcode2) {
		// TODO Auto-generated method stub
		
	}

	private void comf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short)(befehlcode2 & 127);
		//temporaere Variable fuer das Ergebnis
		short tmp = (short) (speicherZellen[f] ^ 255);
		
		if(tmp == 0)
		{
			StatusRegister.setZFlag(true);
		}
		else
		{
			StatusRegister.setZFlag(false);
		}
				
		if(d == 0)
		{
			wRegister = tmp;
		}
		else
		{
			speicherZellen[f] = tmp;
		}	
		
		SingleLayoutController.programcounter ++;
		StatusRegister.setStatus();
	}

	private void incf(short befehlcode2) {
		//Adresse f maskieren auf 7 bit
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		if (StatusRegister.statusBank()){
			f += 128;	
		}
		
		if (speicherZellen[f] >= 254) {
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		
		if ((befehlcode2 & 128) == 0) {
			//Abfrage ob Z-Flag NICHT! gesetzt ist
			if(!StatusRegister.statusZFlag()) {
				wRegister = (short) (speicherZellen[f] += 1);
			}
			else {
				wRegister = 0;
			}
		}
		else {
			//Abfrage ob Z-Flag NICHT! gesetzt ist
			if(!StatusRegister.statusZFlag()) {
				speicherZellen[f] += 1;
			}
			else {
				speicherZellen[f] = 0;
			}
		}
		StatusRegister.setStatus();
		StatusRegister.copyStatus();
		SingleLayoutController.programcounter ++;
	}

	private void decfsz(short befehlcode2) {
		
		short d = (short) (befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		short result = 0;
		
		//Bank 1 oder Bank 0, wenn Bank 1, Adresse um 128 erhoehen
		if(StatusRegister.statusBank()){
				f += 128;
			}
		
		if(speicherZellen[f] == 0){
				result = 255;
			}
			else{
				result = (short) (speicherZellen[f] - 1);
			}
		
		//Ergebnis in W-Register
		if(d == 0){
			wRegister = result;
		}
		//Ergebnis in f
		else{
			speicherZellen[f] = result;
		}
		if (result == 0) {
			nop();
		}
		
		SingleLayoutController.programcounter ++;
		StatusRegister.setStatus();
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
		short b = (short) ((befehlcode2 & 896) >>> 7);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		short wert = (short) (speicherZellen[f] & (int) Math.pow(2,b));
		if(wert != 0){
			speicherZellen[f] -= (short) Math.pow(2,b);
		}
		
		SingleLayoutController.programcounter ++;
		StatusRegister.setStatus();
		
	}

	private void bsf(short befehlcode2) {
		short b = (short) ((befehlcode2 & 896) >>> 7);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		System.out.println("b: " + b + "   f: " +f);
		
		short wert = (short) (speicherZellen[f] & (int) Math.pow(2,b));
		System.out.println("wert: " + wert);
		if(wert == 0){
			speicherZellen[f] += (short) Math.pow(2,b);
			System.out.println("In if");
		}	
		System.out.println("Ende");
		
		SingleLayoutController.programcounter ++;
		StatusRegister.setStatus();
	}

	private void btfsc(short befehlcode2) {
		short b = (short) ((befehlcode2 & 896) >>> 7);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		if((short) (speicherZellen[f] & (int) Math.pow(2,b)) == 0){
			nop();
		}
		
		SingleLayoutController.programcounter ++;
		StatusRegister.setStatus();
	}

	private void btfss(short befehlcode2) {
		short b = (short) ((befehlcode2 & 896) >>> 7);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		if((short) (speicherZellen[f] & (int) Math.pow(2,b)) != 0){
			nop();
		}
		
		SingleLayoutController.programcounter ++;
		StatusRegister.setStatus();
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
		
		SingleLayoutController.programcounter ++;
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
		
		//mit Ueberlauf der hinteren 4 bit
		if((l & 15 + wRegister & 15) >= 16) {
			StatusRegister.setDigitCarryBit(true);
		}
		else {
			StatusRegister.setDigitCarryBit(false);
		}
		
		//mit Ueberlauf ueber 255
		if(wRegister + l > 255)
		{
			//Carry Bit setzen
			StatusRegister.setCarryBit(true);
			if(wRegister + l == 256) {
				StatusRegister.setZFlag(true);
			}
			else {
				StatusRegister.setZFlag(false);
			}
			
			//Nach Ueberlauf wieder bei 0 anfangen
			wRegister = (short) (wRegister + l - 256);
		}
		else {
			//Carry Bit leeren
			StatusRegister.setCarryBit(false);
			wRegister = (short) (wRegister + l);
		}	
		
		SingleLayoutController.programcounter ++;
	}
	
	//f maskieren und auf 0 pruefen. Ggf. Adresse ueber FSR beziehen
	private short maskiereAdresse(short code)
	{
		short adresse = (short) (code & 127);
		if(adresse == 0)
		{
			return (short) speicherZellen[speicherZellen[FSR]];
		}
		return adresse;
	}
	
}
