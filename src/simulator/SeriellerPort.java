package simulator;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
	
public class SeriellerPort {

	//tmp PortA
	private int tmp_portA;
	//tmp PortB
	private int tmp_portB;
	//tmp TrisA
	private int tmp_trisA;
	//tmp TrisB
	private int tmp_trisB;
	    
	//Serieller Port
	SerialPort serialPort;
	
	//Konstruktor: Register inizialisiseren
	public SeriellerPort() {
	    tmp_trisA = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TRISA];
	    tmp_portA = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTA];
	    tmp_trisB = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_TRISB];
	    tmp_portB = BefehlDecoder.speicherZellen[RegisterAdressen.ADR_PORTB];
	}
	
	//Verbindung ueber SerialPort aufbauen (Inizialisierung)
	public void verbindeComport(String portName) throws Exception {
	    CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	    //Pruefen, ob der serielle Port verfuegbar ist
	    if (!portIdentifier.isCurrentlyOwned()) {
	        System.out.println("Port nicht verfuegbar.");
	        serialPort = (SerialPort) portIdentifier.open(this.getClass().getName(), 2000);
	        serialPort.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	        schreibeComport();
	        leseComport();
	       //Serieller Port nicht verfuegbar
	    } else {
	    	System.out.println("Port nicht verfuegbar.");
	    }
	}
	
	//ComPort freigeben / Verbindungsabbau
	public void close() {
	    serialPort.close();
	}
	
	//Alle verfuegbaren seriellen Ports auflisten
	@SuppressWarnings("rawtypes")
	public static List<String> getAllPorts(){
		
		List<String> list = new ArrayList<String>();
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		
		//Portliste aufbauen
		while (portList.hasMoreElements()) {
			CommPortIdentifier portID = (CommPortIdentifier) portList.nextElement();
			if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				list.add(portID.getName());
			}
		}
		return list;
	}
	
	/** Methode um die obersten Bits 4-7 einer Integer-Zahl zu bekommen.
	 * @param value Vom Typ INT enthält die Integer-Zahl.
	 * @return Gibt den Wert der Bits 4-7 als INT zurück.
	 */
	private int getHighNibble(int value) {
	    for (int i = 0; i < 4; i++) {
	        value = value & ~(1 << i);
	    }
	    for (int i = 31; i > 7; i--) {
	        value = value & ~(1 << i);
	    }
	    value = value >> 4;
	    return value;
	}
	
	/** Methode um die obersten Bits 0-3 einer Integer-Zahl zu bekommen.
	 * @param value Vom Typ INT enthält die Integer-Zahl.
	 * @return Gibt den Wert der Bits 0-3 als INT zurück.
	 */
	private int getLowNibble(int value) {
	    for (int i = 31; i > 3; i--) {
	        value = value & ~(1 << i);
	    }
	    return value;
	}
	
	/** Methode um ein Nibble so zu verändern, dass es von der Hardware gelesen werden kann.
	 * @param nibble Vom Typ INT enthält den Wert des Nibbles
	 * @return Gibt den Wert des Nibbles + 0x30h als INT zurück
	 */
	private int setNibbleToSend(int nibble) throws Exception {
	    if (nibble < 0 || nibble > 15) {
	        Exception exception = new Exception();
	        throw exception;
	    }
	    int valueToSend = 0x30 + nibble;
	    return valueToSend;
	}
	
	/** Methode um den im Zwischenspeicher liegenden PortA-Wert auszugeben.
	 * @return Gibt den aktuellen Wert von PortA als INT zurück
	 */
	public int getInputPortA() {
	    return tmp_portA;
	}
	
	/** Methode um den im Zwischenspeicher liegenden PortB-Wert auszugeben.
	 * @return Gibt den aktuellen Wert von PortB als INT zurück
	 */
	    public int getInputPortB() {
	        return tmp_portB;
	    }
	
	  //portA zum HW Simulator schreiben
	    public void portAtoHardware(int portAnewValue) {
	        tmp_portA = portAnewValue;
	        schreibeComport();
	        leseComport();
	    }
	
	  //portB zum HW Simulator schreiben
	    public void portBtoHardware(int portBnewValue) {
	        tmp_portB = portBnewValue;
	        schreibeComport();
	        leseComport();
	    }
	
	  //trisA zum HW Simulator schreiben
	public void trisAtoHardware(int trisAnewValue) {
	    tmp_trisA = trisAnewValue;
	    schreibeComport();
	    leseComport();
	}
	
	//trisB zum HW Simulator schreiben
	public void trisBtoHardware(int trisBnewValue) {
	    tmp_trisB = trisBnewValue;
	    schreibeComport();
	    leseComport();
	}
	
	/** Methode um die Werte von PortA, TrisA, PortB und TrisB als Byte-Array zu bekommen.
	 * Die Daten sidn dabei bereits sendefertig, also mit 0x30h bestückt.
	 * @return Gibt die aktuellen Werte von TrisA, PortA, TrisB und PortB in dieser Reihenfolge
	 * sendefertig als Byte-Array zurück.
	 */
	private byte[] getValuesAsByteArray() {
	    byte data[] = new byte[9];
	    try {
	        data[0] = (byte) setNibbleToSend(getHighNibble(tmp_trisA));    //Tris A 3xH
	        data[1] = (byte) setNibbleToSend(getLowNibble(tmp_trisA));    //Tris A 3xL
	        data[2] = (byte) setNibbleToSend(getHighNibble(tmp_portA));    //Port A 3xH
	        data[3] = (byte) setNibbleToSend(getLowNibble(tmp_portA));    //Port A 3xL
	        data[4] = (byte) setNibbleToSend(getHighNibble(tmp_trisB));    //Tris B 3xH
	        data[5] = (byte) setNibbleToSend(getLowNibble(tmp_trisB));    //Tris B 3xL
	        data[6] = (byte) setNibbleToSend(getHighNibble(tmp_portB));    //Port B 3xH
	        data[7] = (byte) setNibbleToSend(getLowNibble(tmp_portB));    //Port B 3xL
	        data[8] = (byte) '\r'; // CR
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        System.exit(1);
	    }
	    return data;
	}
	
	//PortA, PortB, TrisA und TrisB zum HW Simulator schreiben
	public void schreibeComport() {
	    try {
	        OutputStream out = serialPort.getOutputStream();
	        out.write(getValuesAsByteArray());
	        out.close();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        System.exit(1);
	    }
	}
	
	//PortA und PortB vom HW Simulator lesen
	public void leseComport() {
	    try {
	        InputStream in = serialPort.getInputStream();
	        byte c = 0;
	        short portAB[] = new short[100];
	        int counter = 0;
	        while (c > -1) {
	            c = (byte) in.read();
	            if (c < 0) {
	                break;
	            }
	            portAB[counter] = (short) c;
	            counter++;
	        }
	        if (portAB[0] > 48) {
	            hardwaretoRegister(portAB);
	        }
	        in.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.exit(1);
	    }
	}
	
	//Von HW empfangene Daten speichern 
	private void hardwaretoRegister(short[] portAB) {
	    tmp_portA = ((portAB[0] - 50) << 4) + portAB[1] - 48;
	    tmp_portB = ((portAB[2] - 48) << 4) + portAB[3] - 48;
	}
}
