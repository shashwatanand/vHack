package org.pdpblr.emailagent.client;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	public static void main(String[] args) {
		String to = "sanand@vmware.com";
		String from = "sanand@vmware.com";
		
		//String host = "smtp.gmail.com";
		String host = "localhost";
		Properties properties = System.getProperties();
		//properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.host", host);
		//properties.setProperty("mail.smtp.user", userName);
		Session session = Session.getDefaultInstance(properties);
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("This is mail is sent from Java Client");			
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=GETVMNAMES");
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=GETDATACENTERNAMES");
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=GETHOSTS");
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=GETRESOURCEPOOL");
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=GETHOSTS;VMNNAME=Control-VM-SPS");
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=POWERON_VM;VMNNAME=Dummy-Test");
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=POWEROFF_VM;VMNNAME=Dummy-Test");
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=REBOOT_VM;VMNNAME=Dummy-Test");
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=SUSPEND_VM;VMNNAME=Dummy-Test");
			//message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=RESUME_VM;VMNNAME=Dummy-Test");
			//message.setText("VCIPADDRESS=10.112.73.175;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=GETLOGFILE");
			message.setText("VCIPADDRESS=10.112.178.31;USERNAME=administrator;PASSWORD=ca$hc0w;COMMAND=HOSTVMOTION;VMNNAME=DummyVMForVMOTION;NEWHOSTNAME=km-pdp028.eng.vmware.com");

			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}
