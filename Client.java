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

public class Client implements Runnable
{
   	private Socket socket;
	private Vector<String> currentUsers;
	private PrintWriter out;
	private Scanner in;
   	
	public Client(chatGUI gui)
	{
		// How should we get these
		int port = 0;
		String host = "client";		// Host name
		
		
		// Connect to server
		try 
		{
			socket = new Socket(host, port);
			out = new PrintWriter(socket.getOutputStream());
			// out.println();	// Print the username to notify the server
			out.flush();
			
			// Add username to current users
			// currentUsers.add();
			
			Thread thread = new Thread();
			thread.start();
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
				in = new Scanner(socket.getInputStream());
				out = new PrintWriter(socket.getOutputStream());
				out.flush();
				
				while(true)
				{
					if (in.hasNext())	// Receive a message
					{
						String message = in.nextLine();
						
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
	 */
	public void send(String message)
	{
		// out.println(username + ": " + message);
		out.flush();
	}
	
	/**
	 * Main makes the calls to set up the GUI and the client
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		chatGUI gui = new chatGUI();
		gui.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		new Client(gui);		//Create new chat client
	}
}


