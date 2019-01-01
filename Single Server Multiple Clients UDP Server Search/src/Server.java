import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
	static final int SERVER_PORT = 8888;
	static final String SERVER_MSG = "This is server.";
	static final String CLIENT_MSG = "This is client.";
	ServerSocket ss;
	ArrayList<Socket> clients;
	int clientID = 0;
	int port;
	HandleClientScans listener;
	
	public Server(int p) {
		clients = new ArrayList<Socket>();
		port = p;
		listener = new HandleClientScans();
	}
	
	public void start() {
		try {
			ss = new ServerSocket(port);
			System.out.println("Server created at port " + port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Port " + port + " not available for use.");
		}
		Thread listen = new Thread(new AcceptClients());
		listen.start();
		listener.start();
	}
	
	private class AcceptClients implements Runnable {

		@Override
		public void run() {
			while(true) {
				try {
					Socket client = ss.accept();
					clients.add(client);
					clientID++;
					System.out.println("Client" + clientID + "[" + client.getRemoteSocketAddress() + "] connected");
					Thread handle = new Thread(new handleClient(client, clientID));
					handle.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class handleClient implements Runnable {
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
	
	public void sendToClients(String s) {
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
	
	//TODO add clean closing of server
	private void closeServer() {
		
	}
	
	private class HandleClientScans {
		
		public void start() {
			Thread handle = new Thread(new Handle());
			handle.start();
		}
		
		private class Handle implements Runnable {

			@Override
			public void run() {
				DatagramSocket ds = null;
				try {
					ds = new DatagramSocket(SERVER_PORT, InetAddress.getByName("0.0.0.0"));
					ds.setBroadcast(true);
				} catch (UnknownHostException | SocketException e) {
					e.printStackTrace();
					return;
				}
				while(true) {
					byte[] buffer = new byte[1500];
					DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
					try {
					//	System.out.println("Waiting");
						ds.receive(dp);
					//	System.out.println("Received packet from " + dp.getAddress() + ":" + dp.getPort());
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
					String received = new String(dp.getData(), 0, dp.getLength());
					if(received.contains(CLIENT_MSG)) {
						String send = SERVER_MSG;
						byte[] sendBuffer = send.getBytes();
						try {
							DatagramPacket reply = new DatagramPacket(sendBuffer, sendBuffer.length, dp.getAddress(), dp.getPort());
							ds.send(reply);
					//		System.out.println("Sent packet to " + reply.getSocketAddress());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
