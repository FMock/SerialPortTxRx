import java.util.Observable;

/*
 * Frank Mock
 * Last Updated March 13, 2016
 */

public class SerialSubject extends Observable{
	private String data;
	
	public SerialSubject() { }
	
	public void dataChanged() {
		setChanged();
		notifyObservers(); //each observers update method gets called
	}
	
	public void setData(String data) {
		this.data = data;
		dataChanged();
	}
	
	public String getData() {
		return data;
	}
}
