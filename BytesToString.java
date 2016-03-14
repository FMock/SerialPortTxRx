//demonstrates how to convert an array of bytes back to the characters they represent
public class BytesToString
{
	/**
	 * Utility class to assist in converting ASCII bytes
	 * into Strings
	 * Frank Mock
	 */
	
	/**
	 * Returns the integer value of a byte
	 * @param b the byte of which the integer value will be determined
	 * @return the integer of a byte
	 */
	public int valueOfByte(byte b)
	{
		return Integer.parseInt(String.valueOf(b));
	}
	
	/**
	 * Takes an integer ASCII value and returns its String representation
	 * @param i the integer value to be converted
	 * @return a String that the ASCII integer value represented
	 */
	public String asciiToString(int i)
	{
		StringBuilder sb = new StringBuilder();
		sb.append((char)i);
		return sb.toString();
	}
}