import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;

//textview that gets  updated
@SuppressWarnings("serial")
public class TextAreaView extends JTextArea implements View
{
	public TextAreaView()
	{
		this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		this.repaint();
	}
	
	public void update(String s)
	{
		this.setText(s);
		this.repaint();
	}
	
	public void clear()
	{
		this.setText("");
	}
}
