package org.pdpblr.util;

import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.pdpblr.emailagent.MailInfo;

public class Util {
	static String lastProcessedDate = "Thu, 28 Apr 2011 00:42:49 -0700";
	private static Logger logger = Logger.getLogger(Util.class.getName());
	
	public static boolean isStringEmpty(String inputString) {
		if (inputString != null && !inputString.equals(""))
			return true;
		return false;
	}
	
	/*public static void retrieveMail(String popServer, String popUser, String popPassword) {
		final String SUBJECT_SHOULD_BE = "Process it from Email Server";
		final String FOLDER_TO_LOOK = "Inbox";
		Store store = null;
		POP3Folder folder = null;
		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			store = session.getStore("pop3");
			store.connect(popServer, popUser, popPassword);
			
			folder = (POP3Folder) store.getFolder(FOLDER_TO_LOOK);
			if (folder == null) {
				throw new Exception("No POP3 INBOX");
			}
			
			folder.open(Folder.READ_ONLY);
			System.out.println("Class MailRetriever : Message count in folder is " + folder.getMessageCount());
			Message[] messages = folder.getMessages(folder.getMessageCount() - 10, folder.getMessageCount());
			for (Message message : messages) {
				String currentDate = message.getHeader("Date")[0];
				System.out.println(currentDate);
				System.out.println(message.getSubject());
				if (currentDate.compareToIgnoreCase(lastProcessedDate) > 0 ) {
					lastProcessedDate = message.getHeader("Date")[0];
					System.out.println(message.getSubject());
					System.out.println(lastProcessedDate);
					//Address[] fromAddress = message.getFrom();
					System.out.println(message.getFrom()[0].toString());
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (folder != null) folder.close(false);
				if (store != null) store.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}*/
	
	public static void sendMail(MailInfo mailInfo, Session session) {
		Properties properties = new Properties();
		//String host = "mailhost.vmware.com";
		String host = "smtp.vmware.com";

		String fromAddress = "sanand@vmware.com";
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.debug", "true");
		properties.put("mail.smtp.user", "sanand@vmware.com");
		//properties.put("mail.smtp.starttls.enable", "true");
		//properties.put("mail.smtp.socketFactory.port", "465");
		//properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		//properties.put("mail.smtp.socketFactory.fallback", "false");
		//properties.put("mail.smtp.auth", "true");
		
		try {
			/*Session session = Session.getDefaultInstance(properties, new Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() 
				{ return new PasswordAuthentication("sanand@vmware.com", "Admin1234");}
			});*/
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromAddress));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailInfo.getFromAddress()));
			//message.addRecipient(Message.RecipientType.TO, new InternetAddress("dnanecha@vmware.com"));
			//message.addRecipient(Message.RecipientType.TO, new InternetAddress("bhatr@vmware.com"));
			message.setSubject("This is the Subject Line!, Sent by Java");
			message.setText(mailInfo.getBody());
			
			handleAttachments(mailInfo.getAttachments(), message);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, "sanand@vmware.com", "Admin123");
			transport.sendMessage(message, message.getAllRecipients());
			//Transport.send(message, message.getAllRecipients());
			logger.log(Level.INFO, "Sent message successfully.... to " + mailInfo.getFromAddress());
		} catch (Exception mex) {
			mex.printStackTrace();
		}
	}
	
	/*Command String: VCIPADDRESS=<IPADDRESS>;USERNAME=<USERNAME>;PASSWORD=<PASSWORD>;COMMAND=GETVMNAMES*/
	public static Hashtable<String, String> parseMailBody(String input) {
		Hashtable<String, String> commandTable = new Hashtable<String, String>();
		String[] differentCommands = input.split(";");
		for (String commands : differentCommands) {
			String[] keyValuePair = commands.split("=");
			commandTable.put(keyValuePair[0], keyValuePair[1]);
		}
		return commandTable;
	}
	
	private static void handleAttachments(List<String> attachments, Message message) {
		if (attachments == null || attachments.size() <= 0)
			return;
		
		Multipart multipart = new MimeMultipart();
		BodyPart messageBodyPart;
		for (String file : attachments)
		{
			messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(file);
			try {
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(source.getName());
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
			} catch (MessagingException e) {
				e.printStackTrace();
			}

		}
	}


}
