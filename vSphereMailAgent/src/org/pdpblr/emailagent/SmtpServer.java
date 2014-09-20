package org.pdpblr.emailagent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Shashwat Anand
 */
public class SmtpServer implements Runnable {
	
	private static Logger logger = Logger.getLogger(SmtpServer.class.getName());

	private final ServerSocket connection;
	private final ExecutorService smtpReqHandlerPool;
	
	public SmtpServer(String address, int port, int maxRequestInQueue) throws UnknownHostException, IOException {
		smtpReqHandlerPool = Executors.newCachedThreadPool();
		connection = new ServerSocket(port, maxRequestInQueue, InetAddress.getByName(address));
	}
	
	public void run() {
		try {
			logger.info("SMTP Listener started");
			while(true) {
				try {
					logger.info("Accepting connections.");
					Socket socket = connection.accept();
					SmtpRequestHandler handler = new SmtpRequestHandler(socket);
					smtpReqHandlerPool.execute(handler);
				} catch (IOException ioe) {
					logger.log(Level.SEVERE, "An exception occured while attempting to listen for connections: " + ioe.getMessage(), ioe);
				}
			}
		} catch(Exception ex) {
			logger.log(Level.SEVERE, "An exception occured during the listener loop: " + ex.getMessage(), ex);
			if (smtpReqHandlerPool != null) {
				smtpReqHandlerPool.shutdown();
			}
		}
	}
}
