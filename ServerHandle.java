import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class ServerHandle extends Thread{
	
	private ServerSocket serverSocket = null;
	private Vector<clientThread> clientThreads = null;
	private int portNum;
	private int userCounter; // an implicit naming convention
	private boolean serverContinue;
	private WorkerThread worker;
	//private ConcurrentHashMap<String,clientThread> nameToClient;
	public enum JOBTYPE{ //for job class
		MSG //send message
	}
	
	public ServerHandle(int port) {
		try {
			serverSocket = new ServerSocket(port);
			portNum = serverSocket.getLocalPort();
			System.out.println("Listening on port" + portNum);
		} catch (IOException e) {
			System.err.println("Could not listen on port" + port);
			System.exit(-1);
		}
		clientThreads = new Vector<clientThread>();
		//nameToClient = new ConcurrentHashMap<String,clientThread>();
		
		worker = new WorkerThread();
		worker.start();
		
		serverContinue = true;
		userCounter = 0;
		this.start();
	}
	
	public void run(){
		System.out.println("Running server thread");
		while(serverContinue){
			try {
				serverSocket.setSoTimeout(10000);
				Socket addSocket = serverSocket.accept(); // blocking read
				userCounter++;
				String addUserName = "User" + Integer.toString(userCounter);
				clientThread toAdd = new clientThread(addSocket, addUserName ,worker);
				clientThreads.add(toAdd);
				//nameToClient.put(toAdd.getUserName(),toAdd);//add userName to lookup
				toAdd.start();
				

				System.out.println("Added user" + toAdd.getUserName());
				
			} catch(SocketTimeoutException ste){
				System.out.println("Socket accept user timed out...");
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
		private String userName;
		public ObjectOutputStream out;
		private ObjectInputStream in;
		private WorkerThread worker;
		
		public clientThread(Socket r, String name, WorkerThread worker){
			clientSocket = r;
			this.userName = name;
			this.worker = worker;
			
			try {
				out = new ObjectOutputStream( clientSocket.getOutputStream());
				in = new ObjectInputStream( clientSocket.getInputStream());
				
				out.writeObject(new SchatMessage("Server","Your user name has been set to " + userName));
				out.flush();
			} catch (IOException e) {
				System.err.println("Could not open socket in/out.");
			}
		}
		
		public void run(){
			System.out.println("Starting user thread for " + userName);
			
			while(true){
				//check socket
				try {
					abstractMessage message = (abstractMessage) in.readObject();
					System.out.println("Got a message!");
					if(message.getType() == abstractMessage.MESSAGETYPE.CCHAT){
						CchatMessage tempMessage = (CchatMessage) message;
						System.out.println("Got a CCHAT message with body: " + tempMessage.getBody());
						sendMessagesToWorker(tempMessage);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		private void sendMessagesToWorker(CchatMessage MSG){
			if(MSG.getTo().equals("0")){
				//to everyone
				for(clientThread e : clientThreads){
					System.out.println("Sending " + MSG.getBody() + " to " + e.getUserName());
					worker.JobQueue.add(new Job(new SchatMessage(e.getUserName(),MSG.getBody()),
							this.getUserName()));
				}//for each
			}
		}
		
		public String getUserName(){
			return userName;
		}
	}
	
	private class WorkerThread extends Thread{
		public ConcurrentLinkedQueue<Job> JobQueue;
		
		public WorkerThread(){
			JobQueue = new ConcurrentLinkedQueue<Job>();
		}
		
		public void run(){
			System.out.println("Worker queue starting");
			
			while(true){
				Job working = JobQueue.poll();
				if(working != null){
					if(working.getType() == JOBTYPE.MSG){
						sendMessage((SchatMessage) working.getMSG(), working.getTo());
					}
				}
			}//while
		}
		
		public void sendMessage(SchatMessage MSG,String to){
			clientThread sendTo = null;
			
			for( clientThread t : clientThreads){
				if(t.getUserName().equals(to)){
					sendTo = t;
					break;
				}
			}
			
			try {
				sendTo.out.writeObject(MSG);
				sendTo.out.flush();
			} catch (IOException e) {
				System.out.println("unable to send message");
				System.exit(-1);
			} catch(NullPointerException e){
				System.out.println("No User "+to);
			}
		}
	}
	
	private class Job{
		private JOBTYPE type;
		private abstractMessage message;
		private String to;
		
		public Job(abstractMessage MSG,String sendto){
			type = JOBTYPE.MSG;
			message = MSG;
			to = sendto;
		}
		
		public JOBTYPE getType(){
			return type;
		}
		
		public abstractMessage getMSG(){
			return message;
		}
		
		public String getTo(){
			return to;
		}
	}

}
