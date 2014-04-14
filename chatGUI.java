import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.*;


@SuppressWarnings("serial")
public class chatGUI extends JFrame implements ActionListener{
	
	private JButton serverButton, clientButton;
	private JLabel welcomeLabel;
	private JPanel firstPanel, serverPanel, clientPanel;
	private JButton serverConnect, serverDisconnect,serverExit;
	private JPanel serverButtonPanel;
	private JPanel serverConnects;
	private JPanel serverChatWindow;
	private JPanel serverContainer;
	private JLabel connectLabel,chatLabel;
	private JTextField serverTextArea;
	private JButton clientConnect, clientDisconnect, clientExit;
	private JPanel clientButtonPanel;
	private JPanel clientConnects;
	private JPanel clientChatWindow;
	private JPanel clientContainer;
	private JTextField clientTextArea;
	private JButton serverMultipleMessages;
	private JButton clientMultipleMessages;
	
	
	
	
	
	public chatGUI(){
		
		super("Chat Client");
		setSize(1000,750);
		
		firstPanel = new JPanel();
		
		firstPanel.setLayout(new FlowLayout());
		welcomeLabel = new JLabel("Are you a server or client? ");
		serverButton = new JButton("Server");
		serverButton.addActionListener(this);
		clientButton = new JButton("Client");
		clientButton.addActionListener(this);
		
		
		firstPanel.add(welcomeLabel);
		firstPanel.add(serverButton);
		firstPanel.add(clientButton);
		
		add(firstPanel);
		
		
		
		
		
		
		setVisible(true);
	}
	
	private void setServerGUI(){
		
		remove(firstPanel);
		
		serverPanel=new JPanel(new BorderLayout());
		add(serverPanel);
		
		serverConnect = new JButton("Connect");
		serverConnect.addActionListener(this);
		serverDisconnect = new JButton("Disconnect");
		serverDisconnect.addActionListener(this);
		serverExit = new JButton("Exit");
		serverExit.addActionListener(this);
		serverMultipleMessages = new JButton("Private Message");
		serverMultipleMessages.addActionListener(this);
		
		serverButtonPanel = new JPanel();
		serverButtonPanel.add(serverConnect);
		serverButtonPanel.add(serverDisconnect);
		serverButtonPanel.add(serverExit);
		serverButtonPanel.add(serverMultipleMessages);
		
		serverPanel.add(serverButtonPanel, BorderLayout.NORTH);
		
		serverContainer = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		serverConnects = new JPanel();
		Border connectBorder = BorderFactory.createLineBorder(Color.black);
		serverConnects.setBorder(connectBorder);
		serverConnects.setBackground(Color.WHITE);
		c.gridx=0;
		c.gridy=1;
		c.weightx=0.1;
		c.ipady=600;
		c.ipadx=200;
		
		
		
		serverContainer.add(serverConnects,c);
		
		connectLabel = new JLabel("Connections");
		c.gridx=0;
		c.gridy=0;
		c.ipady=0;
		c.ipadx=0;
		serverContainer.add(connectLabel,c);
		
		serverChatWindow = new JPanel();
		Border chatBorder = BorderFactory.createLineBorder(Color.black);
		serverChatWindow.setBorder(chatBorder);
		serverChatWindow.setBackground(Color.WHITE);
		c.gridx=1;
		c.gridy=1;
		c.ipadx=700;
		c.ipady=600;
		serverContainer.add(serverChatWindow,c);
		
		chatLabel = new JLabel("Chat");
		c.gridx=1;
		c.gridy=0;
		c.ipadx=0;
		c.ipady=0;
		serverContainer.add(chatLabel,c);
		
		serverPanel.add(serverContainer);
		
		

		
		
		
		serverTextArea = new JTextField();
		serverTextArea.addActionListener(this);
		serverPanel.add(serverTextArea,BorderLayout.SOUTH);
		
		
		revalidate();
		
		
		
		
	}
	
	private void setClientGUI(){
		
		remove(firstPanel);
		
		clientPanel=new JPanel(new BorderLayout());
		add(clientPanel);
		
		clientConnect = new JButton("Connect");
		clientConnect.addActionListener(this);
		clientDisconnect = new JButton("Disconnect");
		clientDisconnect.addActionListener(this);
		clientExit = new JButton("Exit");
		clientExit.addActionListener(this);
		
		clientButtonPanel = new JPanel();
		clientButtonPanel.add(clientConnect);
		clientButtonPanel.add(clientDisconnect);
		clientButtonPanel.add(clientExit);
		
		clientPanel.add(clientButtonPanel, BorderLayout.NORTH);
		
		clientContainer = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		clientConnects = new JPanel();
		Border connectBorder = BorderFactory.createLineBorder(Color.black);
		clientConnects.setBorder(connectBorder);
		clientConnects.setBackground(Color.WHITE);
		c.gridx=0;
		c.gridy=1;
		c.weightx=0.1;
		c.ipady=600;
		c.ipadx=200;
		
		
		
		clientContainer.add(clientConnects,c);
		
		connectLabel = new JLabel("Connections");
		c.gridx=0;
		c.gridy=0;
		c.ipady=0;
		c.ipadx=0;
		clientContainer.add(connectLabel,c);
		
		clientChatWindow = new JPanel();
		Border chatBorder = BorderFactory.createLineBorder(Color.black);
		clientChatWindow.setBorder(chatBorder);
		clientChatWindow.setBackground(Color.WHITE);
		c.gridx=1;
		c.gridy=1;
		c.ipadx=700;
		c.ipady=600;
		clientContainer.add(clientChatWindow,c);
		
		chatLabel = new JLabel("Chat");
		c.gridx=1;
		c.gridy=0;
		c.ipadx=0;
		c.ipady=0;
		clientContainer.add(chatLabel,c);
		
		clientPanel.add(clientContainer);
		
		clientTextArea = new JTextField();
		clientTextArea.addActionListener(this);
		clientPanel.add(clientTextArea,BorderLayout.SOUTH);
		
		
		
		
		revalidate();
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == serverButton){
			
			setServerGUI();
			
		}
		
		else if(e.getSource() == clientButton){
			
			setClientGUI();
			
		}
		
		else if(e.getSource() == serverExit){
			System.exit(0);
		}
		else if(e.getSource() == serverConnect){
			String port=(String)
				JOptionPane.showInputDialog("What port do you want to use?");
			
			System.out.println(port);
		}
		else if(e.getSource() == serverDisconnect){
			
		}
		else if(e.getSource() == clientExit){
			System.exit(0);
		}
		else if(e.getSource() == clientConnect){
			String port=(String)
					JOptionPane.showInputDialog("What port do you want to use?");
			System.out.println(port);
		}
		else if(e.getSource() == clientDisconnect){
			
		}
		else if(e.getSource() == serverTextArea){
			String line = serverTextArea.getText();
			System.out.println(line);
			serverTextArea.setText(null);
		}
		else if(e.getSource() == clientTextArea){
			String line = clientTextArea.getText();
			System.out.println(line);
			clientTextArea.setText(null);
			
		}
		else if(e.getSource() == serverMultipleMessages){
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
			
			int result =
					JOptionPane.showConfirmDialog(
							null,
							privateMessagePanel,
							"Enter your message and recipients",
							JOptionPane.OK_CANCEL_OPTION);
			if(result == JOptionPane.OK_OPTION){
				System.out.println(people.getText());
				System.out.println(message.getText());
			}
					
			
			
					
		}
		else{
			System.out.println("Problem with button press");
		}
		
	}

	
	
	
	
	
	
}