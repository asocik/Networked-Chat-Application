import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.*;
import java.io.IOException;
import java.util.Random;


/**************************
 * 
 * @author Ryan Szymkiewicz
 *  written for CS 342, Spring 2014 Project 4
 *  
 *  The purpose of this file is to provide the GUI for our networked
 *  chat application.  There are options for both a server and client GUI
 *  that can be selected when the program is started.
 *
 */
@SuppressWarnings("serial")
public class chatGUI extends JFrame implements ActionListener{

	//these class variables will be used throughout the file.
	//they will be described as they are used

	private JButton serverButton, clientButton;
	private JLabel welcomeLabel;
	private JPanel firstPanel, clientPanel;
	private JButton clientConnect, clientDisconnect, clientExit;
	private JPanel clientButtonPanel;
	private JTextArea clientMessageArea;
	private JButton clientMultipleMessages;
	private JButton serverConnect;
	private JPanel serverPanel;
	private JTextField portTextField;
	private int serverPort;
	private JPanel serverInfoPanel;
	private JPanel textContainer;
	private JButton clientSend;
	private String userName=null;
	private JPanel chatContainer;
	private JTextArea chatArea;
	private JScrollPane chatAreaScroll;
	private JPanel connectionPanel;
	private String privateMessagePeople=null;
	private String privateMessageContent=null;
	private Client client;
	private ServerHandle server;
	private int clientPort;
	private String clientPortString;



	/*******************
	 * The chatGUI method is used when starting the program.  
	 * It allows the user to select if they are a client or server
	 */
	public chatGUI(){

		//set title of window
		super("Chat Client");
		setSize(200,100);//set initial window size

		firstPanel = new JPanel();//create the first panel

		//ask if the user is a server or client
		firstPanel.setLayout(new FlowLayout());
		welcomeLabel = new JLabel("Are you a server or client? ");
		serverButton = new JButton("Server");
		serverButton.addActionListener(this);
		clientButton = new JButton("Client");
		clientButton.addActionListener(this);


		firstPanel.add(welcomeLabel);
		firstPanel.add(serverButton);
		firstPanel.add(clientButton);

		add(firstPanel);//add the panel
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/*******************
	 * The setServerGUI method is used if the user indicates that they are a 
	 * server.  This allows the user to "select" a port and displays that
	 * information
	 */
	private void setServerGUI(){

		remove(firstPanel);//remove the first panel

		setSize(300,200);//set a new size

		//initialize the new panel
		serverPanel = new JPanel(new GridLayout(3,1));
		add(serverPanel);

		JLabel serverWelcome = new JLabel("Enter the port you will connect to");
		serverPanel.add(serverWelcome,SwingConstants.CENTER);

		//show the port being connected to.  currently set to 0(default)
		portTextField = new JTextField();
		portTextField.setText("0");
		serverPanel.add(portTextField);

		//connect button....this will bring up a new screen if clicked
		serverConnect = new JButton("Connect");
		serverConnect.addActionListener(this);
		serverPanel.add(serverConnect);

		revalidate();//change the window
	}


	/***************************
	 * The setUsername method sets the username for a client based
	 * on what they have entered or generates a random username if 
	 * they do not select one
	 */
	private void setUsername()
	{
		//initilize the labels and panel,  add the items
		JLabel nameLabel = new JLabel("Enter your username");
		JTextField nameEnter = new JTextField();
		JLabel portLabel = new JLabel("Enter the port");
		JTextField portEnter = new JTextField();
		JPanel namePanel = new JPanel(new GridLayout(4,1));
		namePanel.add(nameLabel);
		namePanel.add(nameEnter);
		namePanel.add(portLabel);
		namePanel.add(portEnter);

		//use JOptionPane to get the username with an "OK-Cancel" popup
		int option =
				JOptionPane.showConfirmDialog(
						null,
						namePanel,
						"Enter your username and port",
						JOptionPane.OK_CANCEL_OPTION);

		String tUserName = nameEnter.getText();//temporarily set the username
		String tPort = portEnter.getText();//get temporary port

		if(option == JOptionPane.OK_OPTION && !tUserName.isEmpty() &&
				!tPort.isEmpty()){

			userName = tUserName;//if user pressed OK and field is valid,  set name
			clientPort = Integer.parseInt(tPort);//set port

			System.out.print(userName);
			System.out.print(clientPort);

		}

		//if the user pressed cancel or did not enter a name,
		//generate a random username
		else if(option == JOptionPane.CANCEL_OPTION || tUserName.isEmpty()){

			int num1,num2,num3;

			//three random integers from 0-99 are obtained.
			//There is an extremely small chance that two users could
			//have the same name,  but since this chat should not use more than
			//~5 clients,  the risk is very low.
			Random generator = new Random();
			num1 = generator.nextInt(99);
			num2 = generator.nextInt(99);
			num3 = generator.nextInt(99);

			//set the username with User followed by the 3 numbers
			userName = "User"+
					Integer.toString(num1)+
					Integer.toString(num2)+
					Integer.toString(num3);

			System.out.println(userName);
		}
		else if(tPort.isEmpty()){
			JOptionPane.showMessageDialog(this,"Port Not Selected.  Quitting");
			System.exit(0);
		}
	}


	/**********************
	 * setCLientGUI sets up and displays the GUI for a client
	 * The functionality will be described below
	 */
	private void setClientGUI()
	{
		remove(firstPanel);//remove first panel

		setSize(1000,750);//set new size
		setLocationRelativeTo(null);
		setUsername();//set username if client

		//clientPanel is a container for all other panels in this method
		clientPanel=new JPanel(new BorderLayout());
		add(clientPanel);

		//set up buttons that will line the top of the client panel
		//these buttons are set up with action listeners so that they
		//respond when clicked
		clientConnect = new JButton("Connect");
		clientConnect.addActionListener(this);
		clientDisconnect = new JButton("Disconnect");
		clientDisconnect.addActionListener(this);
		clientExit = new JButton("Exit");
		clientExit.addActionListener(this);
		clientMultipleMessages = new JButton("Private Message");
		clientMultipleMessages.addActionListener(this);

		//intialize and add a JPanel that contains the  buttons
		clientButtonPanel = new JPanel();
		clientButtonPanel.add(clientConnect);
		clientButtonPanel.add(clientDisconnect);
		clientButtonPanel.add(clientExit);
		clientButtonPanel.add(clientMultipleMessages);

		//add the panel to the top of the container panel
		clientPanel.add(clientButtonPanel, BorderLayout.NORTH);


		//set up a new JPanel that will be in the middle of the page
		//add some padding around the panel as a border
		chatContainer = new JPanel(new BorderLayout());
		Border panelBorder = 
				BorderFactory.createEmptyBorder(30,30,30,30);
		chatContainer.setBorder(panelBorder);

		//create a new text area on the right side of the page
		chatArea = new JTextArea(5,55);
		chatArea.setEditable(false);

		//add a scroll pane to that text area
		chatAreaScroll = new JScrollPane (chatArea);
		Border chatAreaBorder = BorderFactory.createTitledBorder("Chat Window");
		chatAreaScroll.setBorder(chatAreaBorder);

		//create a new JPanel to keep track of current connections
		connectionPanel = new JPanel();
		connectionPanel.setPreferredSize(new Dimension(225,100));
		//create a border to display what this is
		Border connectionPanelBorder = 
				BorderFactory.createTitledBorder("Chat Connections");
		connectionPanel.setBorder(connectionPanelBorder);
		connectionPanel.setBackground(Color.WHITE);


		//add the components to the container panel
		chatContainer.add(chatAreaScroll,BorderLayout.EAST);
		chatContainer.add(connectionPanel,BorderLayout.WEST);
		clientPanel.add(chatContainer);

		//set up a new panel that will be used to enter messages
		//and send them to the server for display
		textContainer = new JPanel(new BorderLayout());

		//create text area where messages will be typed
		clientMessageArea = new JTextArea();
		Border messageBorder = BorderFactory.createLineBorder(Color.black);
		clientMessageArea.setBorder(messageBorder);
		clientMessageArea.setText("Type Here");

		//add this to the page
		textContainer.add(clientMessageArea,BorderLayout.CENTER);

		//create a send button to send message off
		clientSend = new JButton("Send");
		clientSend.addActionListener(this);
		textContainer.add(clientSend,BorderLayout.EAST);

		//add to the window
		clientPanel.add(textContainer,BorderLayout.SOUTH);

		revalidate();
	}

	/*****************
	 * These getters are used by the server and client
	 * portions of the program
	 * 
	 */
	public int getServerPort(){
		return serverPort;
	}

	public JPanel getConnectWindow(){
		return connectionPanel;
	}

	public JTextArea getChatWindow(){
		return chatArea;
	}

	public String getUsername(){
		return userName;
	}

	public JTextArea getCurrentLine(){
		return clientMessageArea;
	}

	public String getPrivateMessagePoeple(){
		String tempPeople = privateMessagePeople;
		privateMessagePeople=null;
		return tempPeople;
	}

	public String getPrivateMessageContents(){
		String tempMessage = privateMessageContent;
		privateMessageContent = null;
		return tempMessage;
	}

	/*****************
	 * This method allows for interaction when buttons are pressed
	 */
	public void actionPerformed(ActionEvent e) {

		//if server button is pressed,  start GUI for server portion
		if(e.getSource() == serverButton){

			setServerGUI();
			
		}

		//if client button is pressed,  start GUI for client portion
		else if(e.getSource() == clientButton){

			setClientGUI();

		}

		//if using as server,  after connnect has been pressed,
		//set the port to the class variable and switch panels
		//that displays the port
		else if(e.getSource() == serverConnect){
			server = new ServerHandle(0);
			String tempPort = portTextField.getText();
			serverPort = Integer.parseInt(tempPort);
			remove(serverPanel);
			serverInfoPanel = new JPanel();
			add(serverInfoPanel);
			JLabel info = new JLabel("You are connected to port: "+ server.getPort());
			serverInfoPanel.add(info);
			System.out.println(server.getPort());
			revalidate();
		}

		//exit the window
		else if(e.getSource() == clientExit){
			System.exit(0);
		}

		//connect as a client
		else if(e.getSource() == clientConnect){
			client = new Client(clientPort, this);
		}

		//disconnect as a client
		else if(e.getSource() == clientDisconnect){
			try 
			{
				client.disconnect();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		}

		//send a message as a client
		else if(e.getSource() == clientSend)
		{
			String message = clientMessageArea.getText();
			//chatArea.append(message + "\n");

			try 
			{
				client.send(message);
				clientMessageArea.setText("");	// Clears out old text
			} 
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}

		//private message as a client
		//this code was modified from ideas found at:
		//http://stackoverflow.com/questions/6555040/
		//multiple-input-in-joptionpane-showinputdialog
		//The fields are initialized as JTextFields that are placed into
		//a JPanel
		//This panel has a gridLayout and the previous fields are added to the
		//panel.  Then JOptionPane is passed this panel and a 
		//window with the fields and "Cancel" and "OK" appears.
		//If text is entered and OK is pressed,  a private message should be sent
		//this code was modified from the original source
		//through the different text areas that are needed and the way
		//that the panel was set up.
		else if(e.getSource() == clientMultipleMessages){
			JTextField people = new JTextField(25);
			JTextField message = new JTextField(50);
			JLabel peopleLabel = new JLabel("Enter the people you wish to \n"
					+ "chat with separated with a space\n.");
			JLabel messageLabel = new JLabel("Enter your message \n");

			JPanel privateMessagePanel = new JPanel(new GridLayout(4,1));
			privateMessagePanel.add(peopleLabel);
			privateMessagePanel.add(people);

			privateMessagePanel.add(messageLabel);
			privateMessagePanel.add(message);

			int check =
					JOptionPane.showConfirmDialog(
							null,
							privateMessagePanel,
							"Enter your message and recipients",
							JOptionPane.OK_CANCEL_OPTION);
			if(check == JOptionPane.OK_OPTION){
				privateMessagePeople=people.getText();
				privateMessageContent=message.getText();
			}
		}
		else{
			System.out.println("Problem with button press");
		}

	}

	public static void main(String[] args)
	{
		chatGUI gui = new chatGUI();
		gui.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
}