package org.pdpblr.emailagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.pdpblr.util.Util;
import org.pdpblr.vc.VCProxy;

/**
 * @author Shashwat Anand
 */
public class SmtpRequestHandler implements Runnable {
	
	private static Logger logger = Logger.getLogger(SmtpRequestHandler.class.getName());

	private MailInfo mailInfo;
	private Socket _socket;
	private BufferedReader _inStream;
	private PrintWriter _outStream;
	
	public SmtpRequestHandler(Socket socket) throws IOException {
		_socket = socket;
		_inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		_outStream = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	private void println(String str) {
		_outStream.println(str);
		_outStream.flush();
		
		logger.log(Level.INFO, str);
	}
	
	private void doCommand_DATA() throws IOException {
		String fromAddress = "";
		String toAddress = "";
		StringBuilder message = new StringBuilder();
		boolean flagHeadend = false;
		println("354 Send me data. End with.");
		logger.log(Level.INFO, "Mail:");

		do {
			String input = _inStream.readLine();
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
		mailInfo = new MailInfo(toAddress, fromAddress, message.toString());
		println("from DS");
		println(mailInfo.getBody());
		println(mailInfo.getFromAddress());
		println(mailInfo.getToAddress());
		Hashtable<String, String> table = Util.parseMailBody(mailInfo.getBody());
		VCProxy proxy = new VCProxy(table);
		mailInfo.setBody(proxy.processCommand());
		mailInfo.setAttachments(proxy.getAttachments());
		//Util.sendMail(mailInfo);
	}
	
	public void run() {
		println("220 SMTP Server VCMailAgent ready and waiting");
		try {
			do {
				String input = "";
				try {
					input = _inStream.readLine();
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
				if (_socket != null)
				{
					_socket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
