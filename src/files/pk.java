package files;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class pk implements Runnable {
	Socket s;
	// static String
	// sdcard=Environment.getExternalStorageDirectory().getAbsolutePath();
	static String sdcard = "/home";

	public pk(Socket pd) {
		s = pd;
	}

	@Override
	public void run() {
		int length = 100;
		try {
			// s.setSoTimeout(1000);
			InputStream in1 = s.getInputStream();
			byte[] buff = new byte[length];
			int i = 0;
			i = in1.read(buff);
			String str = new String(buff, 0, i);
			System.out.print(str);
			do {
				i = in1.read(buff);
				// if (i > 0) {
				// System.out.print(new String(buff, 0, i));
				// }
				if (i < length)
					break;
			} while (true);

			int p = str.indexOf(' ', 4);
			int c = str.indexOf(' ');
			str = str.substring(c + 1, p);
			if (str.equals("/"))
				str = "";
			str = str.replaceAll("%20", " ");
			File fl = new File(sdcard + str);
			OutputStream pout = new BufferedOutputStream(s.getOutputStream());
			PrintStream out = new PrintStream(pout);
			if (fl.isDirectory())
				showDir(fl, out, str);
			if (fl.isFile())
				showfile(fl, out, str);
			s.close();

		} catch (Exception e) {

			try {
				s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			}
			e.printStackTrace();

		}
	}

	private void showfile(File fl, PrintStream out, String str) {
		// TODO Auto-generated method stub
		out.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + guess(fl.getName()) + "\r\n" + "Server: copy 1.0\r\n\r\n");
		sendFile(fl, out);
		out.println();
		out.flush();
	}

	void sendFile(File fl, OutputStream out) {
		try {
			@SuppressWarnings("resource")
			InputStream file = new FileInputStream(fl);
			int i = 0;
			byte[] buffer = new byte[1000];
			while (file.available() > 0) {
				i = file.read(buffer);
				out.write(buffer, 0, i);
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	private void showDir(File fl, PrintStream out, String str) {

		out.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n" + "Server: copy 1.0\r\n\r\n");
		out.println("<html><title>" + fl.getName() + "</title><body>");
		File[] con = fl.listFiles();
		if(fl.getParent().length()>sdcard.length())
		{
		String sr=fl.getParent().substring(sdcard.length());
	 out.println("<p><a href=\"" + sr + "\">" + "back" + "</a></p>");
		}
		out.println("<table>");
		out.println("<tr>");
		out.println("<td><p>"+"Name:"+ "</a></p></td>");
		out.println("<td><p>"+"Type"+ "</a></p></td>");
		out.println("</tr>");
		for (File k : con) {
			if(k.getName().charAt(0)!='.'){
			out.println("<tr>");
			out.println("<td><p><a href=\"" + str + "/" + k.getName() + "\">" + k.getName() + "</a></p></td>");
			if(k.isDirectory())out.println("<td><p>Director</a></p></td>");
			else out.println("<td><p>File</a></p></td>");
			out.println("</tr>");
		}
		}
		out.println("</body></html>");
		out.println();
		out.flush();
		// System.out.println("end");
	}

	private static String guess(String path) {
		if (path.endsWith(".html") || path.endsWith(".htm"))
			return "text/html";
		else if (path.endsWith(".txt") || path.endsWith(".java"))
			return "text/plain";
		else if (path.endsWith(".gif"))
			return "image/gif";
		else if (path.endsWith(".class"))
			return "application/octet-stream";
		else if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png"))
			return "image/jpeg";
		else
			return "application/app";
	}

}
