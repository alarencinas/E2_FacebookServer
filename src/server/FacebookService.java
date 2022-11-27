package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

public class FacebookService extends Thread  { //for the start
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;
	private HashMap<String, String> hashFacebook = new HashMap<>();

	private static String DELIMITER = "#";
	
	public FacebookService(Socket socket) {
		try {
			this.tcpSocket = socket;
		    this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.start();
			
			//Facebook USers
			hashFacebook.put("felipe@gmail.com", "felipe");
			
		} catch (Exception e) {
			System.err.println("# FacebookService - TCPConnection IO error:" + e.getMessage());
		}
	}

	public void run() {
		try {
			String data = this.in.readUTF();			
			System.out.println("   - FacebookService - Received data from '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data + "'");					
			data = this.checkFacebooKUser(data);
			this.out.writeUTF(data);					
			System.out.println("   - FacebookService - Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data.toUpperCase() + "'");
		} catch (Exception e) {
			System.out.println("   # FacebookService - TCPConnection error" + e.getMessage());
		} finally {
			try {
				tcpSocket.close();
			} catch (Exception e) {
				System.out.println("   # FacebookService - TCPConnection IO error:" + e.getMessage());
			}
		}
	}

	public String checkFacebooKUser(String msg) { 		
		String translation = null;
		
		if (msg != null && !msg.trim().isEmpty()) {
			try {
				StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITER);		
				String email = tokenizer.nextToken();
				String password = tokenizer.nextToken();
				System.out.println("   Starting checking of " + email + " from: " + password);
		
				if (email != null && password != null) {
					if(hashFacebook.containsKey(email)) {
						if(hashFacebook.get(email).matches(password)) {
							return "true";
						}
					}
					
					System.out.println("   - Facebook server result: " + translation);
				}
			} catch (Exception e) {
				System.out.println("   # FacebookService - Facebook API error:" + e.getMessage());
				translation = null;
			}
		}
		
		return "false";
	}
}
