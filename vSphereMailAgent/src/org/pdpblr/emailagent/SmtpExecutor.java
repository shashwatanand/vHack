package org.pdpblr.emailagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.pdpblr.util.Util;
import org.pdpblr.vc.VCProxy;

public class SmtpExecutor extends Thread {
	private MailInfo mailInfo;
	private Socket _socket;
	private BufferedReader _inStream;
	private PrintWriter _outStream;
	
	public SmtpExecutor(Socket socket) throws IOException {
		this._socket = socket;
		this._inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this._outStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	private void println(String str) {
		this._outStream.println(str);
		this._outStream.flush();
		
		System.out.println(str);
	}
	
	private void doCommand_DATA() throws IOException {
		String fromAddress = "";
		String toAddress = "";
		StringBuilder message = new StringBuilder();
		boolean flagHeadend = false;
		println("354 Send me data. End with.");
		System.out.println("Mail:");
		
		do {
			String input = this._inStream.readLine();
			if (".".equals(input)) {
				println("250 OK");
				break;
			} else {
				if (input.equals("")) {
					flagHeadend = true;
				} else if (input.contains("From:")) {
					fromAddress = input.split(" ")[1];
				} else if (input.contains("To:")) {
					toAddress = input.split(" ")[1];
				}
				if (flagHeadend) {
					message.append(input);
				}
				System.out.println(input);
			}
		} while (true);
		this.mailInfo = new MailInfo(toAddress, fromAddress, message.toString());
		println("from DS");
		println(this.mailInfo.getBody());
		println(this.mailInfo.getFromAddress());
		println(this.mailInfo.getToAddress());
		Hashtable<String, String> table = Util.parseMailBody(this.mailInfo.getBody());
		VCProxy proxy = new VCProxy(table);
		mailInfo.setBody(proxy.processCommand());
		//Util.sendMail(mailInfo);
		//Util.mail(mailInfo);
	}
	
	public void run() {
		println("220 SMTP Server VCMailAgent ready and waiting");
		try {
			do {
				String input = "";
				try {
					input = this._inStream.readLine();
					if (input == null) {
						break;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				StringTokenizer tokenizer = new StringTokenizer(input, " :");
				String command = "";
				command = tokenizer.nextToken().toUpperCase();
				
				if ("DATA".compareTo(command) == 0) {
					doCommand_DATA();
					continue;
				}
				
				if ("RCPT".compareTo(command) == 0) {
					println("250 OK, RCPT received");
					continue;
				}
				
				if (("MAIL".compareTo(command) == 0) || ("SEND".compareTo(command) == 0)) {
					println("250 OK, MAIL/SEND received");
					continue;
				}
				
				if ("HELO".compareTo(command) == 0 || "EHLO".compareTo(command) == 0) {
					println ("250 OK");
					continue;
				}
				
				if ("RSET".compareTo(command) == 0) {
					println("250 OK, RSET command");
					continue;
				}
				
				if ("QUIT".compareTo(command) == 0) {
					println("221 SMTP Server VCMailAgent closing transmission channel");
					break;
				}
			} while (true);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				_socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
