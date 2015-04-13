package simulator;

public class BefehlDecoder
{
	private short befehlcode;
	
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
			befehl = (short) (befehlcode & 16256);
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
		// TODO Auto-generated method stub
		
	}

	private void clrw(short befehlcode2) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
	
}
