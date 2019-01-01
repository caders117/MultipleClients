import java.util.Scanner;

public class ServerTest {

	public static void main(String[] args) {
		Server s = new Server(8888);
		s.start();
		
		Scanner scan = new Scanner(System.in);
		String ans;
		while(true) {
			ans = scan.nextLine();
			s.sendToClients(ans);
		}
	}
}
