package simulator;

import simulator.controller.SingleLayoutController;

public class Watchdog {
	
	private static short prescaler_watchdog0 = 0; //Wert des Prescalers
	public static double watchdog = 0;
	
	public static void berechneWatchdog(){
		
		//Prescaler fuer WDT wenn PSA Bit gesetzt ist ermitteln (abhaengig von PS2, PS1 und PS0)
		if(OptionRegister.getPSA()){
			if(!OptionRegister.getPS2() && !OptionRegister.getPS1() && !OptionRegister.getPS0()){
				prescaler_watchdog0 = 1;
			}
			else if(!OptionRegister.getPS2() && !OptionRegister.getPS1() && OptionRegister.getPS0()){
				prescaler_watchdog0 = 2;
			}
			else if(!OptionRegister.getPS2() && OptionRegister.getPS1() && !OptionRegister.getPS0()){
				prescaler_watchdog0 = 4;
			}
			else if(!OptionRegister.getPS2() && OptionRegister.getPS1() && OptionRegister.getPS0()){
				prescaler_watchdog0 = 8;
			}
			else if(OptionRegister.getPS2() && !OptionRegister.getPS1() && !OptionRegister.getPS0()){
				prescaler_watchdog0 = 16;
			}
			else if(OptionRegister.getPS2() && !OptionRegister.getPS1() && OptionRegister.getPS0()){
				prescaler_watchdog0 = 32;
			}
			else if(OptionRegister.getPS2() && OptionRegister.getPS1() && !OptionRegister.getPS0()){
				prescaler_watchdog0 = 64;
			}
			else if(OptionRegister.getPS2() && OptionRegister.getPS1() && OptionRegister.getPS0()){
				prescaler_watchdog0 = 128;
			}
		}
		//PSA nicht gesetzt -> Prescaler gehoert zum Timer0 -> Kein Prescaler fuer WDT
		else{
			prescaler_watchdog0 = 1;
		}
		
		//Watchdog entsprechend der Quarzfrequenz berechnen
		watchdog += ((Interrupt.taktzyklenNeu - Interrupt.taktzyklenAlt) * Laufzeit.quarzfrequenz * 0.000001);
		
		//Watchdog Timeout loest Interrupt aus und cleared TO Bit des Status Registers
		if(watchdog >= prescaler_watchdog0 * 0.0018){
			StatusRegister.setTO(false);
			SingleLayoutController.WDTReset = true;
		}
	}
}
