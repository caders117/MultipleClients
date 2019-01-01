import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Test {
	static int port = 0;

	public static void main(String[] args) throws UnknownHostException {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			ds.connect(InetAddress.getLocalHost(), 8888);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(ds.getLocalAddress());
		System.out.println(ds.getLocalSocketAddress());
		System.out.println(ds.getInetAddress());
		System.out.println(ds.getPort());
		System.out.println(ds.getLocalPort());
		
	//	Thread l = new Thread(new Listener());
	//	l.start();
		/*
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 52611);
		System.out.println("Sent packet to " + dp.getSocketAddress());
		SocketAddress addr = dp.getSocketAddress();
		InetSocketAddress inaddr = (InetSocketAddress) addr;
		System.out.println(inaddr.getAddress().getHostAddress());
		System.out.println(inaddr.getPort());
		*/
	/*	
		try {
			DatagramSocket ds = new DatagramSocket(8888, InetAddress.getLocalHost());
		//	ds.setBroadcast(true);
			String test = "test";
			byte[] buf = test.getBytes();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 52611);
			System.out.println("Sent packet to " + dp.getSocketAddress());
			ds.send(dp);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
*/
	}
	
	static class Listener implements Runnable {

		@Override
		public void run() {
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(50030, InetAddress.getLocalHost());
			} catch (SocketException | UnknownHostException e1) {
				e1.printStackTrace();
			}

			while(true) {
				byte[] buf = new byte[1500];
				//port = ds.getLocalPort();
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					System.out.println("Packet received from " + dp.getSocketAddress());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
