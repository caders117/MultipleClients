import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Scanner;

public class ClientTest {

	public static void main(String[] args) {
		Client c = new Client();
		c.start();
		String ans;
		Scanner scan = new Scanner(System.in);
		while(true) {
			if(scan.hasNextInt()) {
				SocketAddress addr = c.getServers().get(Integer.parseInt(scan.nextLine()));
				System.out.println(addr.toString());
				c.connect(addr);
			} else {
				ans = scan.nextLine();
				if(ans.equals("list")) {
					System.out.println(Arrays.toString(c.getServers().toArray()));
				} else {
					c.send(ans);
				}
			}	
		}
	}
}
