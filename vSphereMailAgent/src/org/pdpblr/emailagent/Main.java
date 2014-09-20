package org.pdpblr.emailagent;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) throws UnknownHostException,
			IOException {
		final int SLEEP_TIME_IN_MILLISECONDS = 10000;
		
		//while (true) {
			try {
				MailRetriever mailRetriever = new MailRetriever("imap.gmail.com", "shashwatanand.saurabh@gmail.com", "0ubharat");
				Thread t = new Thread(mailRetriever);
				t.start();
				Thread.sleep(SLEEP_TIME_IN_MILLISECONDS);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		//}
		/*try {
			logger.log(Level.INFO, "Starting SMTP server at " + address
					+ " on port " + port);
			// TODO Why do we need to start this server in a new thread?
			SmtpServer server = new SmtpServer(address, port, maxRequestInQueue);
			Executors.newSingleThreadExecutor().execute(server);
		} catch (Exception ex) {
			ex.printStackTrace();
		}*/
	}
	
	
}
