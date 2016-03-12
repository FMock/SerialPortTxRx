import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import jssc.SerialPort;
import jssc.SerialPortException;

/*
 * This project uses the Java Simple Serial Connector library
 * https://github.com/scream3r/java-simple-serial-connector.git
 * 
 * Usage: copy jssc.jar to your ..\java\jdk1.8._25\lib\ext
 * and add the jar file to the project build path
 * In Eclipse go to Project\Properties\Java Build Path
 * on the Libraries tab click Add External Jars 
 * and browse to where the jssc.jar folder is located
 */

//GUI setup and serial port info initialization
public class SerialPortReader
{
		static SerialSubject sdmodel;
		static TextAreaView view;
		static SerialPort serialPort;
		
		static SerialPortInfo spi; //Unnecessary?


	public static void main(String[] args)
	{
		sdmodel = new SerialSubject();
		view = new TextAreaView(sdmodel);
		serialPort = new SerialPort("COM3");
		
		spi = new SerialPortInfo("COM3", 1200, 8, 1, 0); //Unnecessary?

		//build GUI
		JFrame frame = new JFrame("Serial Data Reader");
		frame.setSize(650, 700);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JComboBox<String> comPortComboBox = new JComboBox<String>();
		JComboBox<Integer> baudRateComboBox = new JComboBox<Integer>();
		JComboBox<Integer> dataBitsComboBox = new JComboBox<Integer>();
		JComboBox<Integer> stopBitsComboBox = new JComboBox<Integer>();
		JComboBox<Integer> parityComboBox = new JComboBox<Integer>();
		JButton applySettingsButton = new JButton("Apply Settings");
		applySettingsButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						int i = comPortComboBox.getSelectedIndex();
						spi = new SerialPortInfo("COM3", 1200, 8, 1, 0); //Unnecessary?
					}
				});
		
		JPanel settingsPanel = new JPanel();
		Color ltBlue = new Color(100, 200, 200);
		settingsPanel.setBackground(ltBlue);
		settingsPanel.setLayout(new GridLayout(4,5));
		settingsPanel.add(new JLabel("COM Port"));
		settingsPanel.add(new JLabel("Baud Rate"));
		settingsPanel.add(new JLabel("Data Bits"));
		settingsPanel.add(new JLabel("Stop Bits"));
		settingsPanel.add(new JLabel("Parity"));
		settingsPanel.add(comPortComboBox);
		settingsPanel.add(baudRateComboBox);
		settingsPanel.add(dataBitsComboBox);
		settingsPanel.add(stopBitsComboBox);
		settingsPanel.add(parityComboBox);
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(applySettingsButton);
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
		
		JMenuBar menuBar = new JMenuBar();
		//Build the first menu.
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(menu);
		
		//a group of JMenuItems
		JMenuItem menuItem = new JMenuItem("Exit",
		                         KeyEvent.VK_X);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "This doesn't really do anything");
		menu.add(menuItem);
		
		//Build second menu in the menu bar.
		JMenu menu2 = new JMenu("Serial Port");
		menu2.setMnemonic(KeyEvent.VK_P);
		menu2.getAccessibleContext().setAccessibleDescription(
		        "This menu does nothing");
		menuBar.add(menu2);
		
		JMenuItem menuItem2 = new JMenuItem("Settings",
                KeyEvent.VK_S);
		menuItem2.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem2.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menu2.add(menuItem2);
		
		//Build third menu in the menu bar.
		JMenu menu3 = new JMenu("Help");
		menu3.setMnemonic(KeyEvent.VK_H);
		menu3.getAccessibleContext().setAccessibleDescription(
		        "This menu does nothing");
		menuBar.add(menu3);
		
		JMenuItem menuItem3 = new JMenuItem("About",
                KeyEvent.VK_O);
		menuItem3.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem3.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menu3.add(menuItem3);
		
		
		
		
		JButton getDataButton = new JButton("Get Data");
		getDataButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						try
						{
							serialPort.openPort();
							serialPort.setParams(9600, 8, 1, 0);
							while(serialPort.isOpened())
							{
								//send data to the model (aka subject)
								sdmodel.setData(serialPort.readString());
							}
							//serialPort.closePort();//Close serial port
						}
						catch(SerialPortException ex)
						{
							System.out.println(ex);
						}
					}
				});
		
		JButton clearDataButton = new JButton("Stop/Clear");
		clearDataButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						try
						{
							serialPort.closePort();
						}
						catch (SerialPortException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//clear serial data text
						view.clear();
					}
				});
		
			
		Box box1 = Box.createVerticalBox();
		box1.add(view);
		
		Box box2 = Box.createHorizontalBox();
		box2.add(getDataButton);
		box2.add(clearDataButton);
				
		frame.add(settingsPanel, BorderLayout.NORTH);
		frame.add(box1, BorderLayout.CENTER);
		frame.add(box2, BorderLayout.SOUTH);		

		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
	}
}
