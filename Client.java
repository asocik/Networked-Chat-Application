/**------------------------------------------------------------------------
 * 		@author Adam Socik
 * 		April 2014
 * 		CS 342 Software Design
 * 
 * This class connects to the client GUI to display any new messages and 
 * handles communication with the sever.
 * ------------------------------------------------------------------------*/
import java.io.*;
import java.net.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Client extends Thread
{
   	private Socket socket;
	private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
   	private abstractMessage message;
   	private chatGUI gui;
    
	public Client(int port, chatGUI gui)
	{
		this.gui = gui;
		
		// Connect to server
		try 
		{
			socket = new Socket("127.0.0.1", port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			// send call me		
			
			this.start();
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Server not responding");
			System.exit(0);
		}
	}
	
	/**
	 * Communicates messages with the server
	 */
	@Override
	public void run() 
	{
		while(true)
		{
			try 
			{
				message = (abstractMessage) in.readObject();
				
				// Receive message from the server and output to GUI
				if (message.getType() == abstractMessage.MESSAGETYPE.SCHAT)	 
				{
					SchatMessage serverMessage = (SchatMessage) message;
					gui.getChatWindow().append(serverMessage.getFrom() + ": " + serverMessage.getBody() + "\n");
				}
				
				// Send username to the server
				else if (message.getType() == abstractMessage.MESSAGETYPE.CALLME) 
				{
					String userName = gui.getUsername();
					//out.writeObject(new CallMeMessage(userName));
				}
				
				// Booted by the server
				else if (message.getType() == abstractMessage.MESSAGETYPE.DEAD)	
				{
					disconnect();
				}
				
				// Update the list of users
				else if (message.getType() == abstractMessage.MESSAGETYPE.RESP) 
				{
					
					StringBuilder builder = new StringBuilder();
					
					/*
						Loop over RESP String
							builder.append(username + "\n")
							
						gui.getConnectWindow().add(new JLabel(builder.toString()));
						
					*/	
					RespMessage msg = (RespMessage) message;
					System.out.println(msg.getPayload());
					gui.getConnectWindow().add(new JLabel(msg.getPayload()));
				}
				
			} 
			catch (ClassNotFoundException | IOException e) 
			{
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Disconnects the user from the server - called from the chatGUI class
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException
	{
		// out.writeObject(new ByeMessage());

		JOptionPane.showMessageDialog(null, "You disconnected from the server");
		System.exit(0);
	}
	
	/**
	 * Sends a message to the server
	 * 
	 * @param message 
	 * @throws IOException 
	 */
	public void send(String msg) throws IOException
	{
		String to = "0";
		String body = null;
		
		if (gui.getPrivateMessagePoeple() != null)
		{
			to = gui.getPrivateMessagePoeple();
			body = gui.getPrivateMessageContents();
		}
		else
		{
			body = msg;
		}
			
		out.writeObject(new CchatMessage(to,body));
		out.flush();
	}
}


