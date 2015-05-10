package simulator;

//Klasse fuer die Zuordnung der speziellen Register zu ihrer Adresse in der Speichertabelle

public class RegisterAdressen {
	public static final short ADR_TMR0		= 1;	//Timer 0
	
	public static final short ADR_OPTIONREG	= 129;	//Option Register
	
	public static final short ADR_PCL_0 	= 2; 	//PCL Bank 0
	public static final short ADR_PCL_1 	= 130; 	//PCL Bank 1
	
	public static final short ADR_STATUSREGISTER_0 	= 3; 	//Status Register Bank 0
	public static final short ADR_STATUSREGISTER_1 	= 131; 	//Status Register Bank 1
	
	public static final short ADR_FSR_0 	= 4;	//FSR Bank 0
	public static final short ADR_FSR_1 	= 132;	//FSR Bank 1
	
	public static final short ADR_PORTA 	= 5;	//PortA
	
	public static final short ADR_PORTB 	= 6;	//PortB
	
	public static final short ADR_TRISA 	= 133;	//TrisA
	public static final short ADR_TRISB	= 134;	//TrisB
	
	public static final short ADR_EEDATA 	= 8;	//EEDATA
	
	public static final short ADR_EEADR 	= 9;	//EEADR
	
	public static final short ADR_EECON1 	= 136;	//EECON1
	
	public static final short ADR_EECON2 	= 137;	//EECON2
	
	public static final short ADR_PCLATH_0 	= 10; 	//PCLATH Bank 0
	public static final short ADR_PCLATH_1 	= 138; 	//PCLATH Bank 1
	
	public static final short ADR_INTCON_0 	= 11; 	//INTCON Bank 0
	public static final short ADR_INTCON_1 	= 139; 	//INTCON Bank 1
}
