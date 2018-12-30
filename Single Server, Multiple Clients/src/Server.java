import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	ServerSocket ss;
	ArrayList<Socket> clients;
	int clientID = 0;
	
	public Server(int port) {
		clients = new ArrayList<Socket>();
		try {
			ss = new ServerSocket(port);
			System.out.println("Server created at port " + port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Port " + port + " not available for use.");
		}
		Thread listen = new Thread(new AcceptClients());
		listen.start();
	}
	
	private class AcceptClients implements Runnable {

		@Override
		public void run() {
			while(true) {
				try {
					Socket client = ss.accept();
					clients.add(client);
					clientID++;
					Thread handle = new Thread(new handleClient(client, clientID));
					handle.start();
					Thread out = new Thread(new ProcessOutput());
					out.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class handleClient implements Runnable {
		PrintWriter out;
		BufferedReader in;
		Socket client;
		int ID;
		
		public handleClient(Socket s, int id) {
			client = s;
			ID = id;
			
		}

		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				Thread in = new Thread(new ProcessInput());
				in.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private class ProcessInput implements Runnable {
			String input;
			
			@Override
			public void run() {
				while(true) {
					try {
						input = in.readLine();
					} catch (IOException e) {
						break;
					}
					if(input != null)
						System.out.println("Client" + ID + ": " + input);
				}		
				clients.remove(client);
				System.out.println("Client" + ID + " closed");

			}
		}
	}
	

	private class ProcessOutput implements Runnable {
		Scanner scan = new Scanner(System.in);
		String send = "";

		@Override
		public void run() {
			while(true) {
				send = scan.nextLine();
			//	System.out.println(clients.size());
				if(send.equals("close"))
					closeServer();
				sendToClients(send);
			}
		}
	}
	
	private void sendToClients(String s) {
		PrintWriter out;
		for(Socket client : clients) {
			try {
				out = new PrintWriter(client.getOutputStream(), true);
				out.println(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void closeServer() {
		
	}
}
