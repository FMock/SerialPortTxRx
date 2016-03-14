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
import jssc.SerialPortList;

/*
 /*
 * Frank Mock
 * Last Updated March 13, 2016
 * 
 * A simple GUI program to receive serial data from a serial port.
 * 
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
public class SerialPortReader implements Runnable
{
		static String comPort;
		static SerialSubject sdmodel;
		static TextAreaView view;
		static SerialPort serialPort;
		static SerialPortInfo spi; //Unnecessary?
		
		//Starts the thread that will initiate the reception of serial data
		public void run()
		{
			getSerialData();
		}
		
		//Opens serial port, receives data, converts to String, and passes to Observable
		static void getSerialData()
		{
			BytesToString bts = new BytesToString();
			try
			{
				serialPort.openPort();
				serialPort.setParams(9600, 8, 1, 0);//always setParams after opening port

				String input = "";
				
				while(serialPort.isOpened())
				{
					byte[] i = serialPort.readBytes(1);
					input += bts.asciiToString(bts.valueOfByte(i[0]));
					System.out.println(input);
					sdmodel.setData(input);
				}
			}
			catch(SerialPortException ex)
			{
				System.out.println(ex);
			}
		}

	//Beginning of Main program
	public static void main(String[] args)
	{
		String initialMessage = "";
		
	    String[] portNames = SerialPortList.getPortNames();

	    if(portNames.length > 0)
	    {
	    	comPort = portNames[0]; //assign first port to comPort (allow user to choose in later update)
			initialMessage = "Connected to " 
					+ comPort + System.getProperty("line.separator")
					+ "Press RX Data button below to receive data.";
	    }
	    else
	    {
			initialMessage = "Not connected to a serial port";
	    }
		
		sdmodel = new SerialSubject();
		view = new TextAreaView(sdmodel);
		sdmodel.setData(initialMessage);
		serialPort = new SerialPort(comPort);
		
		spi = new SerialPortInfo(comPort, 9600, 8, 1, 0); //Unnecessary?

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
						spi = new SerialPortInfo(comPort, 9600, 8, 1, 0); //Unnecessary?
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
		
		JMenuItem menuItem2 = new JMenuItem("Receive Serial Data",
                KeyEvent.VK_S);
		menuItem2.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem2.getAccessibleContext().setAccessibleDescription(
				"Get Serial Data");
		menuItem2.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						(new Thread(new SerialPortReader())).start();
					}
				});
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
		
		
		
		/*
		 * Button pressed starts the thread that reads serial data
		 * from serial port
		 */
		JButton getDataButton = new JButton("RX Data");
		getDataButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						(new Thread(new SerialPortReader())).start();
					}
				});
		
		/*
		 * Button pressed closes the currently opened serial port
		 */
		JButton stopDataButton = new JButton("Stop");
		stopDataButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						if(serialPort.isOpened())
						{
							try
							{
								serialPort.closePort();
							}
							catch (SerialPortException e) {
								e.printStackTrace();
							}
						}
					}
				});
		
		/*
		 * Clears the text area (the view is cleared)
		 */
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						//clear serial data text
						view.clear();
					}
				});
			
		Box box1 = Box.createVerticalBox();
		box1.add(view);
		
		Box box2 = Box.createHorizontalBox();
		box2.add(getDataButton);
		box2.add(stopDataButton);
		box2.add(clearButton);
				
		frame.add(settingsPanel, BorderLayout.NORTH);
		frame.add(box1, BorderLayout.CENTER);
		frame.add(box2, BorderLayout.SOUTH);		

		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
	}
}
