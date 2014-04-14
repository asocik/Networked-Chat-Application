import java.io.Serializable;

/**
 * @author jmarav3
 *
 */
public final class CchatMessage extends abstractMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	private final String to;
	private final String body;

	/**
	 * @param to a comma separated list of recipients
	 * @param body the actual message in text
	 */
	public CchatMessage(String to, String body) {
		super(MESSAGETYPE.CCHAT);
		this.to = to;
		this.body = body;
	}
	
	public String getTo(){
		return to;
	}
	
	public String getBody(){
		return body;
	}

}
