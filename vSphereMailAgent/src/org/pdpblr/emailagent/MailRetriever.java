package org.pdpblr.emailagent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

public class MailRetriever implements Runnable{
	String popServer;
	String popUser;
	String popPassword;
	
	static Date lastProcessedDate = null;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
	private Session session;
	static {
		try {
			lastProcessedDate = dateFormat.parse("Thu, 28 Apr 2011 00:42:49 -0700");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public MailRetriever(String popServer, String popUser, String popPassword) {
		this.popServer = popServer;
		this.popUser = popUser;
		this.popPassword = popPassword;
	}
	
	public void run(){
		retrieveMail();
	}
		
	public void retrieveMail() {
		//final String SUBJECT_SHOULD_BE = "VC Ops Request";
		final String FOLDER_TO_LOOK = "Inbox";
		Store store = null;
		Folder folder = null;
		try {
			Properties props = new Properties();
			props.setProperty("mail.imap.host", "imap.gmail.com");
			props.setProperty("mail.imap.port", "993");
			props.setProperty("mail.imap.connectiontimeout", "5000");
			props.setProperty("mail.imap.timeout", "5000");
			session = Session.getInstance(props);
			store = session.getStore("imaps");
			store.connect(popServer, popUser, popPassword);
			
			folder = store.getFolder(FOLDER_TO_LOOK);
			if (folder == null) {
				throw new Exception("No POP3 INBOX");
			}
			
			folder.open(Folder.READ_ONLY);
			System.out.println("Class MailRetriever : Message count in folder is " + folder.getMessageCount());
			Message[] messages = folder.getMessages(folder.getMessageCount() - 1, folder.getMessageCount());
			for (Message message : messages) {
				String currentDateString = message.getHeader("Date")[0];
				Date currentDate = dateFormat.parse(currentDateString);
				System.out.println(currentDate);
				System.out.println(message.getSubject());
				if (message.getSubject().contains("VCIPADDRESS") && currentDate.getTime() > lastProcessedDate.getTime()) {
					lastProcessedDate = dateFormat.parse(message.getHeader("Date")[0]);
					System.out.println(message.getSubject());
					System.out.println(lastProcessedDate);
					System.out.println(message.getFrom()[0].toString());
					process(message);
				}
				/*if (SUBJECT_SHOULD_BE.equalsIgnoreCase(message.getSubject()) && currentDate.compareToIgnoreCase(lastProcessedDate) > 0 ) {
					lastProcessedDate = message.getHeader("Date")[0];
					System.out.println(message.getSubject());
					System.out.println(lastProcessedDate);
					//Address[] fromAddress = message.getFrom();
					System.out.println(message.getFrom()[0].toString());
				}*/
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
	}
	
	
	public void process(Message message){
		try {
			/*String mimeType = message.getHeader("Content-Type")[0];
			DataHandler dataHandler = new DataHandler(this.message.getContent(), mimeType);
			InputStream is = dataHandler.getInputStream();
			StringBuffer sb = new StringBuffer();
			int c = is.read();
			while (c != -1) {
				sb.append((char)c);
			}*/
			//folder = message.getFolder();
			//folder.open(Folder.READ_ONLY);
			Multipart multipart = (Multipart)message.getContent();
			for (int index = 0; index < multipart.getCount(); index++) {
				BodyPart bodyPart = multipart.getBodyPart(index);
				InputStream stream = bodyPart.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(stream));
				while (br.ready()) {
					System.out.println(br.readLine());
				}
				System.out.println();
			}
			System.out.println();
			//System.out.println(sb.toString());
			MailInfo mailInfo = new MailInfo(message.getFrom()[0].toString(), message.getReplyTo()[0].toString(), message.getSubject());
			//Hashtable<String, String> table = Util.parseMailBody(mailInfo.getBody());
			//VCProxy proxy = new VCProxy(table);
			//mailInfo.setBody(proxy.processCommand());
			//mailInfo.setAttachments(proxy.getAttachments());
			mailInfo.setBody("Dummy");
			//Util.sendMail(mailInfo, this.session);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
}