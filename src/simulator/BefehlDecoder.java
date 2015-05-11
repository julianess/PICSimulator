package simulator;

//Diese Klasse bildet den Befehl Decoder.
//Ueber die Angaben aus dem Datenblatt des PIC wird der Befehlcode maskiert
//Die ggf. im Befehlcode beinhalteten Variablen werden ebenfalls maskiert und im Befehl verwendet

import simulator.controller.SingleLayoutController;

public class BefehlDecoder
{	
	private static final short FSR = 4;
	private short befehlcode;
	public static short wRegister;
	
	//Gesamte Speichertabelle wird als Array dargestellt
	public static short[] speicherZellen = new short[256];
	
	public BefehlDecoder (){} //leerer Konstruktor

	public short getBefehlcode() {
		return befehlcode;
	}

	public void setBefehlcode(short befehlcode) {
		this.befehlcode = befehlcode;
	}
	
	//Anhand der Bitmaske wird zunaechst lediglich ermittelt um welchen Befehl es sich handelt
	public void decode(short befehlcode)
	{
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
			//Kein Befehl verunden, da eindeutig
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

	//Hier beginnen die einzelnen Befehle des PIC
	//Je nach Angaben aus dem Datenblatt des PIC werden Programcounter und Taktzyklen berechnet.
	
	private void nop() {
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void returnBefehl() {
		Stack.getStack();
		SingleLayoutController.taktzyklen += 2;
	}

	private void retfie() {
		//GIE Bit setzen
		Intcon.setGIE(true);
		
		//Ruecksprungadresse holen
		Stack.getStack();
		
		//Taktzyklen um 2 erhoehen
		SingleLayoutController.taktzyklen = (Interrupt.cyclesAlt + 2);
		
	}

	private void sleep() {
		// TODO Auto-generated method stub
		
	}

	private void clrwdt() {
		StatusRegister.setTO(true);
		StatusRegister.setPD(true);
		
		Watchdog.watchdog = 0;
	}

	private void movwf(short befehlcode2) {
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		speicherZellen[f] = wRegister;
		StatusRegister.speicherInStatus();
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void clrw(short befehlcode2) {
		wRegister = 0;
		
		StatusRegister.setZFlag(true);
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void clrf(short befehlcode2) {
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		speicherZellen[f] = 0;
		StatusRegister.speicherInStatus();
		
		StatusRegister.setZFlag(true);
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void subwf(short befehlcode2) {
		short d = (short) (befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		short zwComp = (short) ((speicherZellen[f] ^ 255)+1); //Zweierkomplement -> Gleiche Methode wie addwf
		short ergebnisSUBWF = (short) (wRegister + zwComp);
		
		
		//mit Ueberlauf Ueber 255
		if(wRegister + zwComp > 255)
		{	
			//Wenn d == 0, Ergebnis in wRegister, sonst in f
			if(d == 0)
			{
				wRegister = (short) (ergebnisSUBWF - 256);
			}
			else
			{
				speicherZellen[f] = (short) (ergebnisSUBWF - 256);
				StatusRegister.speicherInStatus();
			}

			//Carry Bit setzen
			StatusRegister.setCarryBit(true);
			
			if(wRegister + zwComp == 256) {
				StatusRegister.setZFlag(true);
			}
			else {
				StatusRegister.setZFlag(false);
			}
		}
		else {
			//Wenn d == 0, Ergebnis in wRegister, sonst in f
			if(d == 0)
			{
				wRegister = (short) (ergebnisSUBWF);
			}
			else
			{
				speicherZellen[f] = (short) (ergebnisSUBWF);
				StatusRegister.speicherInStatus();
			}
			StatusRegister.setCarryBit(false); //Carry Bit leeren
		}
		
		//Pruefen ob DC Bit gesetzt werden muss
		if((zwComp & 15 + wRegister & 15) >= 16) {
			StatusRegister.setDigitCarryBit(true);
		}
		else {
			StatusRegister.setCarryBit(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void decf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short)(befehlcode2 & 127);
		
		short ergebnisDECF = (short) (speicherZellen[f] - 1) ;
		
		if(ergebnisDECF == -1){
			ergebnisDECF = 255;
		}
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0){
			wRegister = ergebnisDECF;
		}
		else{
			speicherZellen[f] = ergebnisDECF;
			StatusRegister.speicherInStatus();
		}
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(ergebnisDECF == 0){
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void iorwf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short)(befehlcode2 & 127);
		
		short ergebnisIOR = (short) (wRegister | speicherZellen[f]);
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0){
			wRegister = ergebnisIOR;
		}
		else{
			speicherZellen[f] = ergebnisIOR;
			StatusRegister.speicherInStatus();
		}
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(ergebnisIOR == 0){
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void andwf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short)(befehlcode2 & 127);
		
		short ergebnisAND = (short) (wRegister & speicherZellen[f]);
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0){
			wRegister = ergebnisAND; 
		}
		else{
			speicherZellen[f] = ergebnisAND;
			StatusRegister.speicherInStatus();
		}
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(ergebnisAND == 0){
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void xorwf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short)(befehlcode2 & 127);
		
		//Exclusive OR von wRegister und f
		short ergebnisXORWF = (short) (wRegister ^ speicherZellen[f]);
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0){
			wRegister = ergebnisXORWF; 
		}
		else{
			speicherZellen[f] = ergebnisXORWF;
			StatusRegister.speicherInStatus();
		}
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(ergebnisXORWF == 0){
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void addwf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short)(befehlcode2 & 127);
		
		short ergebnisADDWF = (short) (speicherZellen[f] + wRegister);
		boolean carry = false;
		boolean dcarry = false;
		
		if(((speicherZellen[f] & 15) + (wRegister & 15)) >= 16){
			dcarry = true;
		}
		
		if(ergebnisADDWF > 255){
			ergebnisADDWF = (short) (ergebnisADDWF - 256);
			carry = true;
		}
		
		if(d == 0){
			wRegister = ergebnisADDWF;
		}
		else{
			speicherZellen[f] = ergebnisADDWF;
		}
		
		//Pruefen, ob Carry Bit gesetzt werden muss
		if(carry){
			StatusRegister.setCarryBit(true);
		}
		else{
			StatusRegister.setCarryBit(false);
		}
		
		//Pruefen, ob Digit Carry Bit gesetzt werden muss
		if(dcarry){
			StatusRegister.setDigitCarryBit(true);
		}
		else{
			StatusRegister.setDigitCarryBit(false);
		}
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(ergebnisADDWF == 0){
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void movf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short)(befehlcode2 & 127);
		
		short ergebnisMOVF = speicherZellen[f];
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0){
			wRegister = ergebnisMOVF;
		}
		else{
			speicherZellen[f] = speicherZellen[f];
			StatusRegister.speicherInStatus();
		}
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(ergebnisMOVF == 0){
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void comf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short)(befehlcode2 & 127);
		
		short ergebnisCOMF = (short) (speicherZellen[f] ^ 255);
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0)
		{
			wRegister = ergebnisCOMF;
		}
		else
		{
			speicherZellen[f] = ergebnisCOMF;
			StatusRegister.speicherInStatus();
		}	
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(ergebnisCOMF == 0)
		{
			StatusRegister.setZFlag(true);
		}
		else
		{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void incf(short befehlcode2) {
		short d = (short)(befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		short ergebnisINCF = (short) (speicherZellen[f] + 1);
		
		//Auf Ueberlauf pruefen
		if(ergebnisINCF == 256){
			ergebnisINCF = 0;
		}
		
		if(d == 0){
			wRegister = ergebnisINCF;
		}
		else{
			speicherZellen[f] = ergebnisINCF;
			StatusRegister.speicherInStatus();
		}
		
		if(ergebnisINCF == 0){
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
		
	}

	private void decfsz(short befehlcode2) {
		
		short d = (short) (befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		short ergebnisDECFSZ = 0;
		
		if(speicherZellen[f] == 0){
				ergebnisDECFSZ = 255;
			}
			else{
				ergebnisDECFSZ = (short) (speicherZellen[f] - 1);
			}
		
		//Ergebnis in W-Register
		if(d == 0){
			wRegister = ergebnisDECFSZ;
		}
		//Ergebnis in f
		else{
			speicherZellen[f] = ergebnisDECFSZ;
			StatusRegister.speicherInStatus();
		}
		
		
		if (ergebnisDECFSZ == 0) {
			nop();
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void rrf(short befehlcode2) {
		short d = (short) (befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		short ergebnisRRF = (short) (speicherZellen[f] >> 1);
		
		short tmpCarry = 0; //temporaere Variable fuer CarryBit
		
		//Auf Ueberlauf pruefen (LB)
		if((speicherZellen[f] & 1) == 0){
			tmpCarry = 0;
		}
		else{
			tmpCarry = 1;
		}
		
		//wenn Carry Bit gesetzt ist rechtes Bit des Ergebnis setzen (+1), da nach Shift rechts 0
		if(StatusRegister.getCarryBit()){
			ergebnisRRF += 128;
		}
		
		//Ergebnis auf 8 Bit beschraenken: mit 255 verunden
		ergebnisRRF = (short) (ergebnisRRF & 255);
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0){
			wRegister = ergebnisRRF;
		}
		else{
			speicherZellen[f] = ergebnisRRF;
			StatusRegister.speicherInStatus();
		}
		
		//CarryBit setzen oder loeschen, je nach Ueberlauf (HB)
		if(tmpCarry == 0){
			StatusRegister.setCarryBit(false);
		}
		else{
			StatusRegister.setCarryBit(true);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void rlf(short befehlcode2) {
		short d = (short) (befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		short ergebnisRLF = (short) (speicherZellen[f] << 1);
		short tmpCarry = 0; //temporaere Variable fuer CarryBit
		
		//Auf Ueberlauf pruefen (HB)
		if((speicherZellen[f] & 128) == 0){
			tmpCarry = 0;
		}
		else{
			tmpCarry = 1;
		}
		
		//wenn Carry Bit gesetzt ist rechtes Bit des Ergebnis setzen (+1), da nach Shift rechts 0
		if(StatusRegister.getCarryBit()){
			ergebnisRLF += 1;
		}
		
		//Ergebnis auf 8 Bit beschraenken: mit 255 verunden
		ergebnisRLF = (short) (ergebnisRLF & 255);
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0){
			wRegister = ergebnisRLF;
		}
		else{
			speicherZellen[f] = ergebnisRLF;
			StatusRegister.speicherInStatus();
		}
		
		//CarryBit setzen oder loeschen, je nach Ueberlauf (HB)
		if(tmpCarry == 0){
			StatusRegister.setCarryBit(false);
		}
		else{
			StatusRegister.setCarryBit(true);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void swapf(short befehlcode2) {
		short d = (short) (befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		//mit Bits 4-7 verunden und 4 Bits nach rechts shiften
		short lowSWAPF = (short) ((speicherZellen[f] & 240)  >> 4);
		
		//mit Bits 0-3 verunden und 4 Bits nach links shiften
		short highSWAPF = (short) ((speicherZellen[f] & 15) << 4);
		
		//Ergebnis zusammensetzen
		short ergebnisSWAPF = (short) (lowSWAPF + highSWAPF);
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0){
			wRegister = ergebnisSWAPF;
		}
		else{
			speicherZellen[f] = ergebnisSWAPF;
			StatusRegister.speicherInStatus();
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void incfsz(short befehlcode2) {
		short d = (short) (befehlcode2 & 128);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		short ergebnisiNCFSZ = (short) (speicherZellen[f] + 1);
		
		//Auf Ueberlauf pruefen
		if(ergebnisiNCFSZ == 256){
			ergebnisiNCFSZ = 0;
		}
		
		//Wenn d == 0, Ergebnis in wRegister, sonst in f
		if(d == 0){
			wRegister = ergebnisiNCFSZ;
		}
		else{
			speicherZellen[f] = ergebnisiNCFSZ;
			StatusRegister.speicherInStatus();
		}
		
		//Wenn Ergebnis == 0, dann nop ausfuehren -> naechster Befehl wird uebersprungen
		if(ergebnisiNCFSZ == 0){
			nop();
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void bcf(short befehlcode2) {
		short b = (short) ((befehlcode2 & 896) >>> 7);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		//Durch AND pruefen, ob Bit b von f gesetzt ist, oder nicht
		short wert = (short) (speicherZellen[f] & (int) Math.pow(2,b));
		
		//Wenn Bit b in f gesetzt ist, loeschen
		if(wert != 0){
			speicherZellen[f] -= (short) Math.pow(2,b);
		}
		StatusRegister.speicherInStatus();
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void bsf(short befehlcode2) {
		short b = (short) ((befehlcode2 & 896) >>> 7);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		short wert = (short) (speicherZellen[f] & (int) Math.pow(2,b));
		if(wert == 0){
			speicherZellen[f] += (short) Math.pow(2,b);
		}	
		StatusRegister.speicherInStatus();
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void btfsc(short befehlcode2) {
		short b = (short) ((befehlcode2 & 896) >>> 7);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		if((short) (speicherZellen[f] & (int) Math.pow(2,b)) == 0){
			nop();
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void btfss(short befehlcode2) {
		short b = (short) ((befehlcode2 & 896) >>> 7);
		short f = maskiereAdresse(befehlcode2); //(short) (befehlcode2 & 127);
		
		if((short) (speicherZellen[f] & (int) Math.pow(2,b)) != 0){
			nop();
		}
		
		StatusRegister.speicherInStatus();
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void call(short befehlcode2) {
		short k = (short) (befehlcode2 & 2047);
		Stack.writeStack();
		//PC wird zusammengesetzt aus PCLATH (12-11) und k (10-0)
		Programcounter.pcCallGoto(k);
		SingleLayoutController.taktzyklen += 2;
	}

	private void gotoBefehl(short befehlcode2) {
		//Richtigen PC evtl noch implementieren
		short k = (short) (befehlcode2 & 2047);
		Programcounter.pcCallGoto(k);
		SingleLayoutController.taktzyklen += 2;
	}

	private void movlw(short befehlcode2) {
		wRegister = (short) (befehlcode2 & 255);
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void retlw(short befehlcode2) {
		short k = (short)(befehlcode2 & 255);
		wRegister = k;
		Stack.getStack();
		SingleLayoutController.taktzyklen += 2;
	}

	private void iorlw(short befehlcode2) {
		short k = (short)(befehlcode2 & 255);
		
		//IOR wRegister und k, Ergebnis in wRegister
		wRegister = (short) (wRegister | k);
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(wRegister == 0)
		{
			StatusRegister.setZFlag(true);
		}
		else
		{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void andlw(short befehlcode2) {
		short k = (short)(befehlcode2 & 255);
		
		//AND wRegister und k, Ergebnis in wRegister
		wRegister = (short) (wRegister & k);
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(wRegister == 0){
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void xorlw(short befehlcode2) {
		short k = (short)(befehlcode2 & 255);
		
		//XOR wRegister und k, Ergebnis in wRegister
		wRegister = (short) (wRegister ^ k);
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(wRegister == 0){
			StatusRegister.setZFlag(true);
		}
		else{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void sublw(short befehlcode2) {
		short k = (short)(befehlcode2 & 255);
		
		//XOR wRegister und k, Ergebnis in wRegister
		wRegister = (short) (k - wRegister);
		
		
		//Ergebnis berechnen
		if(k - wRegister < 0){
			wRegister = (short) (256 + (k - wRegister)); //negative Zahl von 256 abziehen ergibt das Ergebnis
		}
		else{
			wRegister = (short) (k - wRegister);
		}
		
		//Pruefen, ob DC gesetzt werden muss (Ueberlauf der unteren 4 Bit)
		if(((k & 15) - (wRegister & 15)) < 0){
			StatusRegister.setDigitCarryBit(true);
		}
		else{
			StatusRegister.setDigitCarryBit(false);
		}
		
		//Pruefen, ob C gesetzt werden muss (Ueberlauf insgesamt)
		if((k - wRegister) < 0){
			StatusRegister.setCarryBit(true);
		}
		else{
			StatusRegister.setCarryBit(false);
		}
		
		//Wenn Ergebnis == 0, Z-Flag setzen, sonst loeschen
		if(wRegister == 0)
		{
			StatusRegister.setZFlag(true);
		}
		else
		{
			StatusRegister.setZFlag(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}

	private void addlw(short befehlcode2) {
		
		//Literal l maskieren
		short l = (short) (befehlcode2 & 255);
		
		//mit Ueberlauf ueber 255
		if(wRegister + l > 255)
		{	
			//Nach Ueberlauf wieder bei 0 anfangen
			wRegister = (short) (wRegister + l - 256);
			
			//Carry Bit setzen
			StatusRegister.setCarryBit(true);
			if(wRegister + l == 256) {
				StatusRegister.setZFlag(true);
			}
			else {
				StatusRegister.setZFlag(false);
			}
		}
		else {
			wRegister = (short) (wRegister + l);
			
			//Carry Bit leeren
			StatusRegister.setCarryBit(false);
		}
		
		//Pruefen, ob DC Bit gesetzt werden muss
		if((l & 15 + wRegister & 15) >= 16) {
			StatusRegister.setDigitCarryBit(true);
		}
		else {
			StatusRegister.setDigitCarryBit(false);
		}
		
		Programcounter.pc ++;
		SingleLayoutController.taktzyklen ++;
	}
	
	//f maskieren und auf 0 pruefen. Ggf. Adresse ueber FSR beziehen
	private short maskiereAdresse(short code)
	{
		short adresse = (short) (code & 127);
		if(adresse == 0)
		{
			return (short) speicherZellen[FSR];
		}
		//Fuer jede Adressierung abpruefen, ob Bank 0 oder Bank 1. Wenn Bank 1, dann adresse +127
		if (StatusRegister.getBank()) {
			adresse += 128;
		}
		return adresse;
	}
}
