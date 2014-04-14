import java.io.Serializable;

/**
 * @author jmarav3
 *
 */
public abstract class abstractMessage implements Serializable {
	
	public enum MESSAGETYPE{
		SCHAT, //a chat message from server to client
		CCHAT, //a chat message from client to server
		CALLME, //a request to set a user name TODO
		INFO, //a client is requesting information TODO
		RESP, //the server is responding to a request TODO
		BYE, //client is disconnecting TODO
		DEAD //the server is dropping a client TODO
	}

	private static final long serialVersionUID = 1L;
	private MESSAGETYPE type;
	
	public abstractMessage(MESSAGETYPE t) {
		type = t;
	}
	
	public MESSAGETYPE getType(){
		return type;
	}
}
