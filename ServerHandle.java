import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Queue;
import java.util.Vector;


public class ServerHandle extends Thread{
	
	private ServerSocket serverSocket = null;
	private Vector<clientThread> clientThreads = null;
	private int portNum;
	private int userCounter; // an implicit naming convention
	private boolean serverContinue;
	
	public ServerHandle(int port) {
		try {
			serverSocket = new ServerSocket(port);
			portNum = serverSocket.getLocalPort();
			System.out.println("Listening on port" + portNum);
		} catch (IOException e) {
			System.err.println("Could not listen on port" + port);
			System.exit(-1);
		}
		
		serverContinue = true;
		userCounter = 0;
		this.start();
	}
	
	public void run(){
		System.out.println("Running thread");
		while(serverContinue){
			try {
				serverSocket.setSoTimeout(10000);
				Socket addSocket = serverSocket.accept(); // blocking read
				userCounter++;
				clientThread toAdd = new clientThread(addSocket, Integer.toString(userCounter));
				clientThreads.add(toAdd);
				toAdd.start();
				

				System.out.println("Added user" + toAdd.getUserName());
				
			} catch(SocketTimeoutException ste){
				System.out.println("Socket timed out...");
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}//while
	}
	
	public int getPort(){
		return portNum;
	}

	private class clientThread extends Thread{
		private Socket clientSocket;
		public Queue<Job> JobQueue;
		private String userName;
		
		public clientThread(Socket r, String name){
			clientSocket = r;
			this.userName = name;
		}
		
		public void run(){
			System.out.println("Starting user thread for " + userName);
		}
		
		public String getUserName(){
			return userName;
		}
	}
	
	private class workerThread extends Thread{
		public Queue<Job> JobQueue;
	}
	
	private class Job{
		
	}
}
