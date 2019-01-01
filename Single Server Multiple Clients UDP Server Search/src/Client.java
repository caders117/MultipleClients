import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
	static final int SERVER_PORT = 8888;
	static final String SERVER_MSG = "This is server.";
	static final String CLIENT_MSG = "This is client.";
	PrintWriter out;
	BufferedReader in;
	ArrayList<SocketAddress> serverList;
	ScanNetwork scanner;
	
	public Client() {
		serverList = new ArrayList<SocketAddress>();
		scanner = new ScanNetwork();
		System.out.println("Client created");
	}
	
	public ArrayList<SocketAddress> getServers() {
		return serverList;
	}
	
	public void start() {
		scanner.start();
		System.out.println("Scan for servers started.");
	}
	
	public void connect(SocketAddress addr) {
		InetSocketAddress inaddr = (InetSocketAddress) addr;
		connect(inaddr.getAddress().getHostAddress(), inaddr.getPort());
	}
	
	public void connect(String addr, int p) {
		try {
			Socket soc = new Socket(addr, p);
			System.out.println("Client started on " + soc.getLocalSocketAddress());
			System.out.println("Connected to " + addr + ":" + p);
			try {
				out = new PrintWriter(soc.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				Thread input = new Thread(new ProcessInput());
				input.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Connection failed");
		}	
	}
	
	public void send(String msg) {
		out.println(msg);
	}
	
	//TODO add clean closing of client
	public void close() {
		
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
					System.out.println("Server: " + input);
			}			
			System.out.println("Server closed");
		}
	}
	
	private class ScanNetwork {
		DatagramSocket ds;
		
		public void start() {
			try {
				ds = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			}
			Thread listen = new Thread(new Listen());
			Thread broadcast = new Thread(new Broadcast());
			listen.start();
			broadcast.start();
		}
		
		private class Listen implements Runnable {

			@Override
			public void run() {
				while(true) {
					byte[] buf = new byte[1500];
					//port = ds.getLocalPort();
					DatagramPacket dp = new DatagramPacket(buf, buf.length);
					try {
						ds.receive(dp);
					//	System.out.println("Packet received from " + dp.getSocketAddress());
					} catch (IOException e) {
						e.printStackTrace();
					}
					String received = new String(dp.getData(), 0, dp.getLength());
					if(received.contains(SERVER_MSG))
						if(!serverList.contains(dp.getSocketAddress()))
							serverList.add(dp.getSocketAddress());
					//	System.out.println(received);
				}
			}
		}
		
		private class Broadcast implements Runnable {

			@Override
			public void run() {
				try {
					ds.setBroadcast(true);
				} catch (SocketException e1) {
					e1.printStackTrace();
					ds.close();
					return;
				}
				InetAddress networkAddress = null;
				try {
					networkAddress = InetAddress.getByName("255.255.255.255");
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
					ds.close();
					return;
				}
				String send = CLIENT_MSG;
				byte[] buffer = send.getBytes();
			//	System.out.println(ds.getLocalPort());
				while(true) {
					DatagramPacket dp = new DatagramPacket(buffer, buffer.length, networkAddress, SERVER_PORT);
					try {
						ds.send(dp);
				//		System.out.println("Sent from " + ds.getLocalSocketAddress());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
