import java.io.Serializable;

/**
 * @author jmarav3
 *
 */

public final class SchatMessage extends abstractMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String from;
	private final String body;

	/**
	 * @param from the user name of the sender of the message
	 * @param body the actual text of the message
	 */
	public SchatMessage(String from, String body) {
		super(MESSAGETYPE.SCHAT);
		this.from = from;
		this.body = body;
	}
	
	public String getFrom(){
		return from;
	}
	
	public String getBody(){
		return body;
	}

}
