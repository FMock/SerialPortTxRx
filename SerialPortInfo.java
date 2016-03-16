//The object that holds current serial port info
public class SerialPortInfo
{
	/**
	 * Frank Mock
	 * Last Updated March 16, 2016
	 * 
	 * This class holds the current values of a Serial Port's parameters
	 */
	private String comPort;
	private int baudRate, dataBits, stopBits, parity;
	
	public SerialPortInfo(String cp, int br, int db, int sb, int p)
	{
		comPort = cp;
		baudRate = br;
		dataBits = db;
		stopBits = sb;
		parity = p;
	}
	
	public String getComPort(){ return comPort; }
	public void setCommPort(String s){ comPort = s; }
	
	public int getBaudRate(){ return baudRate; }
	public void setBaudRate(int i){ baudRate = i; }
	
	public int getDataBits(){ return dataBits; }
	public void setDataBits(int db){ dataBits = db; }
	
	public int getStopBits(){ return stopBits; }
	public void setStopBits(int sb){ stopBits = sb; }
	
	public int getParity(){ return parity; }
	public void setParity(int p){ parity = p; }
	
	public String toString()
	{
		return "COM Port = " + comPort + System.getProperty("line.separator") +
			   "Baud Rate = " + baudRate + System.getProperty("line.separator") +
			   "Data Bits = " + dataBits + System.getProperty("line.separator") +
			   "Stop Bits = " + stopBits + System.getProperty("line.separator") +
			   "Parity = " + parity + System.getProperty("line.separator");
	}
}
