
public class tester {

	public tester() {
		ServerHandle dup = new ServerHandle(0);
		int port = dup.getPort();
		Client clientdup = new Client(port);
	}

	public static void main(String[] args) {
		tester dup = new tester();

	}

}
