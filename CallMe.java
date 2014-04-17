/**
 * @author Adam Socik
 *
 */
import java.io.Serializable;

public final class CallMe extends abstractMessage implements Serializable
{

	private static final long serialVersionUID = 1L;
	private final String username;

	public CallMe (String name)
	{
		super(MESSAGETYPE.CALLME);
		this.username = name;
	}
	
	public String getName()
	{
		return username;
	}
}
