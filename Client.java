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
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Client extends Thread
{
   	private Socket socket;
	private Vector<String> currentUsers;
	private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
   	private CchatMessage messageToServer;
   	private abstractMessage messageFromServer;
    
	public Client(int port)
	{
		String host = "127.0.0.1";		// Host name
		
		// Connect to server
		try 
		{
			socket = new Socket(host, port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			// out.println();	// Print the username to notify the server
			//out.flush();
			
			// Add username to current users
			// currentUsers.add();
			
			//Thread thread = new Thread();
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
		try
		{
			try 
			{
				/*
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream());
				out.flush();
				*/
				
				while(true)
				{
					messageFromServer = (abstractMessage) in.readObject();
					
					if (messageFromServer.getType() == abstractMessage.MESSAGETYPE.SCHAT)	// Receive a message
					{
						SchatMessage tempMessage = (SchatMessage) messageFromServer;
						System.out.println("client got Message");
						System.out.println(tempMessage.getBody());
				
						System.out.println("sending hello");
						out.writeObject(new CchatMessage("0","Hello world?"));
						out.flush();
						/*
						 * If the message contains a command like "!@#" then
						 * there is a new user that needs to be added to the current users.
						 * This is just an idea, we can talk about it later
						 
						 if (message.contains("!@#")
						 {
						 	parse the message for the new user
						 	add to the user list
						 	update the gui
						 }
						 else	// Regular message
						 {
						 	print the message to the GUI
						 }
						 */
					}
				}	
			} catch (ClassNotFoundException e) {
				//abstract class not found
			} 
			finally
			{
				socket.close();
			}
		}
		catch (IOException e) 
		{
			// Print error
		}
	}
	
	/**
	 * Disconnects the user from the server - called from the chatGUI class
	 * 
	 * @throws IOException
	 */
	public void disconnect() throws IOException
	{
		//out.println(username + "has disconnected.");
		out.flush();
		socket.close();
		in.close();
		out.close();
		JOptionPane.showMessageDialog(null, "You disconnected from the server");
		System.exit(0);
	}
	
	/**
	 * Sends a message to the server
	 * 
	 * @param message 
	 * @throws IOException 
	 */
	public void send(String message) throws IOException
	{
		// out.println(username + ": " + message);
		out.flush();
	}
}


