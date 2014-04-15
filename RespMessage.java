import java.io.Serializable;

/**
 * 
 */

/**
 * @author jmarav3
 *
 */
public final class RespMessage  extends abstractMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	private String Payload;
	/**
	 * @param Pay represents the payload to send out
	 */
	public RespMessage(String Pay) {
		super(MESSAGETYPE.RESP);
		Payload = Pay;
	}
	
	public String getPayload()
	{ return Payload; }

}
