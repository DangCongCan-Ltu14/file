package files;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;

public class serv {
	public static void main(String[] args) {
		ServerSocket ser;
		try {
			 Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces();
	         while (eni.hasMoreElements()) {
	              NetworkInterface ni = eni.nextElement();
	              Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
	              while (inetAddresses.hasMoreElements()) {
	                  InetAddress ia = inetAddresses.nextElement();
	                  System.out.println(ni.getName() + "   IP: " + ia.getHostAddress()+"\n");
	             }
	         }
	         
			ser = new ServerSocket(7777);
			while (true) {
				new Thread(new pk(ser.accept())).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
