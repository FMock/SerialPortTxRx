import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

 /**
 * Frank Mock
 * Last Updated December 21, 2016
 * 
 * A simple GUI program to receive serial data from a serial port.
 * 
 * This project uses the Java Simple Serial Connector library
 * File name is jssc.jar
 * https://github.com/scream3r/java-simple-serial-connector.git
 * 
 * Usage: 
 * 2 options to include jssc.jar
 * 1) copy jssc.jar to your ..\java\jdk1.8._25\lib\ext
 * 	  and add the jar file to the project build path by
 *    doing the following. In Eclipse go to 
 *    Project\Properties\Java Build Path, then
 *    on the Libraries tab click Add External Jars 
 *    and browse to where the jssc.jar folder is located
 *    
 *    or
 *    
 * 2) In Eclipse IDE, paste jscc.jar into your project. Right-
 *    click on the jar file, the select Build Path > Add To
 *    Build Path.
 */

//GUI setup and serial port info initialization
public class SerialPortReader implements Runnable
{
		static String comPort;
		static SerialSubject sdmodel;
		static TextAreaView view;
		static JTextArea output;
		static SerialPort serialPort;
		static SerialPortInfo spi;
		static JComboBox<Integer> baudRateComboBox;
		static JComboBox<String> comPortComboBox;
		static JComboBox<Integer> dataBitsComboBox;
		static JComboBox<Integer> stopBitsComboBox;
		static JComboBox<Integer> parityComboBox;
		static JButton getDataButton;
		static JButton stopDataButton;
		static JLabel txMessage;
		static ArrayList<String> listPortNames = new ArrayList<String>();
		
		//The available baud rates
		static int[] baudRates = {1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200};
		//The available data bits
		static int[] dataBits = {5, 6, 7, 8, 9};
		//The available parity bits
		static int[] parityBits =  {0, 1};
		//The available stop bits
		static int[] stopBits = {0, 1};
		
		//Starts the thread that will initiate the reception of serial data
		public void run()
		{
			getSerialData();
		}
		
		/**
		 * Adjusts the SerialPortInfo object parameters to comboBox user choices.
		 * Settings take affect the next time the serial port is opened.
		 */
		public static void setParameters()
		{
			spi.setCommPort(listPortNames.get(comPortComboBox.getSelectedIndex()));
			spi.setBaudRate(baudRates[baudRateComboBox.getSelectedIndex()]);
			spi.setDataBits(dataBits[dataBitsComboBox.getSelectedIndex()]);
			spi.setStopBits(stopBits[stopBitsComboBox.getSelectedIndex()]);
			spi.setParity(parityBits[parityComboBox.getSelectedIndex()]);
			
			sdmodel.setData(spi.toString()); //Send new settings to view for display
		}
		
		//Opens serial port, receives data, converts to String, and passes to Observable
		static void getSerialData()
		{
			BytesToString bts = new BytesToString();
			try
			{
				stopDataButton.setEnabled(true);//enable the stop button so the RX can be stopped
				getDataButton.setEnabled(false);
				serialPort.openPort();
				//JSSC API requires calling setParams after opening port (not in reverse order)
				setParameters();
				serialPort.setParams(spi.getBaudRate(), spi.getDataBits(), spi.getStopBits(), spi.getParity());
				txMessage.setText("");
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
	    for(String s : portNames)
	    	listPortNames.add(s);
	    

	    if(portNames.length > 0)
	    {
	    	comPort = portNames[0]; //assign first port to comPort (allow user to choose in later update)
			initialMessage = "Connected to " 
					+ comPort + System.getProperty("line.separator")
					+ "Press the Open button to receive data.";
	    }
	    else
	    {
			initialMessage = "Not connected to a serial port";
	    }
		
		sdmodel = new SerialSubject();
		view = new TextAreaView(sdmodel);
		sdmodel.setData(initialMessage);
		serialPort = new SerialPort(comPort);
		spi = new SerialPortInfo(comPort, 9600, 8, 1, 0); //set to default values

		//build GUI
		JFrame frame = new JFrame("Serial Data Reader");
		frame.setSize(650, 700);
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		comPortComboBox = new JComboBox<String>();
		for(String s : portNames)
			comPortComboBox.addItem(s);
		comPortComboBox.setSelectedItem(comPort);
		
		
		baudRateComboBox = new JComboBox<Integer>();
		for(int i : baudRates)
			baudRateComboBox.addItem(i);
		baudRateComboBox.setSelectedItem(9600);
			
		dataBitsComboBox = new JComboBox<Integer>();
		for(int i : dataBits)
			dataBitsComboBox.addItem(i);
		dataBitsComboBox.setSelectedItem(8);
			
		stopBitsComboBox = new JComboBox<Integer>();
		for(int i : stopBits)
			stopBitsComboBox.addItem(i);
		stopBitsComboBox.setSelectedItem(1);
		
		parityComboBox = new JComboBox<Integer>();
		for(int i : parityBits)
			parityComboBox.addItem(i);
		parityComboBox.setSelectedItem(0);
		
		JButton applySettingsButton = new JButton("Apply");
		applySettingsButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						setParameters();
					}
				});
		
		JPanel settingsPanel = new JPanel();
		Color ltBlue = new Color(200, 220, 220); //Red, Green, Blue
		settingsPanel.setBackground(ltBlue);
		settingsPanel.setLayout(new GridLayout(4,6));
		settingsPanel.add(new JLabel("COM Port"));
		settingsPanel.add(new JLabel("Baud Rate"));
		settingsPanel.add(new JLabel("Data Bits"));
		settingsPanel.add(new JLabel("Stop Bits"));
		settingsPanel.add(new JLabel("Parity"));
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(comPortComboBox);
		settingsPanel.add(baudRateComboBox);
		settingsPanel.add(dataBitsComboBox);
		settingsPanel.add(stopBitsComboBox);
		settingsPanel.add(parityComboBox);
		settingsPanel.add(applySettingsButton);
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
		settingsPanel.add(new JLabel(""));
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
		JMenuItem menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        		KeyEvent.VK_1, ActionEvent.ALT_MASK));
		
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "Exits the application");
		
		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(serialPort.isOpened())
					try {
						serialPort.closePort();
					} catch (SerialPortException e1) {
						e1.printStackTrace();
					}
				System.exit(0); //exit the application
			}
		});
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
		
		/* Display message dialog box when user clicks on
		 * the About menu item.
		 */
		menuItem3.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		JOptionPane.showMessageDialog(frame,
        				"Version 0.01 \n by Frank Mock 2016\n"
        				+ "http://www.frankmock.com",
        				"Serial Data Reader",
        				JOptionPane.PLAIN_MESSAGE);
        	}});
		
		menu3.add(menuItem3);
		
		
		
		/*
		 * Button pressed starts the thread that opens the serial port and 
		 * reads serial data from the port
		 */
		getDataButton = new JButton("Open");
		getDataButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						(new Thread(new SerialPortReader())).start();
					}
				});
		settingsPanel.add(getDataButton);
		
		
		/*
		 * Button pressed closes the currently opened serial port
		 */
		stopDataButton = new JButton("Close");
		stopDataButton.setEnabled(false);
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
							stopDataButton.setEnabled(false);
							getDataButton.setEnabled(true);
						}
					}
				});
		settingsPanel.add(stopDataButton);
		
		
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
		settingsPanel.add(clearButton);
		
		
		/*
		 * The transmit panel will contain the components related to the
		 * action of the user transmitting text out the serial port
		 */
		JPanel transmitPanel = new JPanel();
		transmitPanel.setBackground(ltBlue);
		transmitPanel.setLayout(new GridLayout(2,1));

		/*
		 * Transmits string out the serial port
		 */
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent ae)
					{
						if(serialPort.isOpened())
						{
							//transmit text entered in output text area
							try
							{
								String s = output.getText();
								serialPort.writeBytes(s.getBytes());
							}
							catch (SerialPortException e)
							{
								System.out.println(e.getMessage());
							}
						}
						else
						{
							txMessage.setText("Serial Port Not Open");
						}
					}
				});	

		
		/*
		 * output is the text area that the user types text into to
		 * transmit out the serial port
		 */
		output = new JTextArea();
		output.setText("F");
		output.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		Box box4 = Box.createHorizontalBox();
		Component horzSpace = Box.createHorizontalStrut(10);
		box4.add(horzSpace);
		box4.add(new JLabel("Enter Text Below To Transmit"));
		Component horzSpace2 = Box.createHorizontalStrut(30);
		box4.add(horzSpace2);
		box4.add(sendButton);
		Component horzSpace3 = Box.createHorizontalStrut(30);
		box4.add(horzSpace3);
		txMessage = new JLabel(""); //Used to display message if port is not open
		txMessage.setForeground(Color.RED);
		txMessage.setFont(new Font("Arial", Font.BOLD, 16));
		box4.add(txMessage);
		Box box5 = Box.createHorizontalBox();
		box5.add(output);
		transmitPanel.add(box4);
		transmitPanel.add(box5);
		
		//Create a box to hold the view in which the serial data will be displayed
		Box box1 = Box.createVerticalBox();
		
		//Add vertical scroll bar to view
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.getViewport().add(view);
		jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		//Add the Scroll Pane that contains the view to box1
		box1.add(jScrollPane);
				
		Box box3 = Box.createHorizontalBox();
		box3.add(transmitPanel);
		
		frame.add(settingsPanel, BorderLayout.NORTH);
		frame.add(box1, BorderLayout.CENTER);
		frame.add(box3, BorderLayout.SOUTH);

		frame.setJMenuBar(menuBar);
		frame.setVisible(true);
	}
}
