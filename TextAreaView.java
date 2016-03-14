import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

/*
 * Frank Mock
 * Last Updated March 13, 2016
 */

@SuppressWarnings("serial")
public class TextAreaView extends JTextArea implements Observer
{
	private String data;
	
	/**
	 * TextAreaView registers itself as an observer to an Observable object.
	 * The Observable will automatically update this observer will it
	 * receives updated date.
	 * @param observable the Observable object this view will observe
	 */
	public TextAreaView(Observable observable)
	{
		this.setText("TextAreaView Created!");	
		observable.addObserver(this);
		this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		this.repaint();
	}
	
	/**
	 * Update is required to be implemented by Observer
	 * @param observable The Observable object that is informing this
	 * observer that new data is available.
	 */
	public void update(Observable observable, Object arg)
	{
		//it's possible to observe multiple Observables so check which one called
		if (observable instanceof SerialSubject)
		{
			SerialSubject serialSubject = (SerialSubject)observable;
			this.data = serialSubject.getData();
			this.setText(data);
			this.repaint();			
		}
	}
	
	//Clears the text from the text area
	public void clear()
	{
		this.setText("");
	}
}
