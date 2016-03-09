//The object that holds current serial port info
public class SerialPortInfo
{
	private String commPort;
	private int baudrate, databits, stopbits, parity;
	
	public SerialPortInfo(String cp, int br, int db, int sb, int p)
	{
		commPort = cp;
		baudrate = br;
		databits = db;
		stopbits = sb;
		parity = p;
	}
	
	public String getCommPort(){ return commPort; }
	public void setCommPort(String s){ commPort = s; }
	
	public int getBaudRate(){ return baudrate; }
	public void setBaudRate(int i){ baudrate = i; }
	
	public int getDataBits(){ return databits; }
	public void setDataBits(int db){ databits = db; }
	
	public int getStopBits(){ return stopbits; }
	public void setStopBits(int sb){ stopbits = sb; }
	
	public int getParity(){ return parity; }
	public void setParity(int p){ parity = p; }
}
