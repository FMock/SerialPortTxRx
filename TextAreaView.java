import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

//textview that gets  updated
@SuppressWarnings("serial")
public class TextAreaView extends JTextArea implements Observer
{
	Observable observable;
	private String data;
	
	public TextAreaView(Observable observable)
	{
		this.observable = observable;
		observable.addObserver(this);
		this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		this.repaint();
	}
	
	//Required to implement this by Observer
	public void update(Observable obs, Object arg)
	{
		if (obs instanceof SerialSubject)
		{
			SerialSubject serialSubject = (SerialSubject)obs;
			this.data = serialSubject.getData();
			this.setText(data);
			this.repaint();			
		}
	}
	
	
	public void clear()
	{
		this.setText("");
	}
}
