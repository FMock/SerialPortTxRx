import java.util.ArrayList;
import jssc.SerialPort;
import jssc.SerialPortException;

public class Serial_Data_Model
{
	String data; //the data that changes
	ArrayList<View> views; //holds the views that register with this model
	byte[] buffer;
	SerialPortInfo spi;
	
	public Serial_Data_Model(SerialPortInfo s)
	{
		views = new ArrayList<View>();
		data = "";
		spi = s;
	}
	
	public void addView(View v)
	{
		views.add(v);
	}
	
	public void getData()
	{
		data = "";//clear data for new data
		SerialPort serialPort = new SerialPort(spi.getCommPort());
		try
		{
			serialPort.openPort();
			serialPort.setParams(spi.getBaudRate(), spi.getDataBits(), spi.getStopBits(), spi.getParity());
			buffer = serialPort.readBytes(10);
			for(byte b : buffer)
			{
				int i = valueOfByte(b);
				data += asciiToString(i);//updating the data
			}
			System.out.println(data);
			serialPort.closePort();
		}
		catch(SerialPortException ex)
		{
			System.out.println("Error opening Com Port" + ex);
		}
		
		//update each view with the new String data
		for(View v : views)
			v.update(data);
	}
	
	public void clearEachView()
	{
		for(View v: views)
			v.clear();
	}
	
	public byte[] charsToBytes(char[] ca)
	{
		int size = ca.length;
		byte[] ba = new byte[size];

		for(int i = 0; i < size; i++)
			ba[i] = (byte)ca[i];
		
		return ba;
	}
	
	public int valueOfByte(byte b)
	{
		return Integer.parseInt(String.valueOf(b));
	}
	
	//convert ascii value to the character it represents
	public String asciiToString(int i)
	{
		StringBuilder sb = new StringBuilder();
		sb.append((char)i);
		return sb.toString();
	}
}
