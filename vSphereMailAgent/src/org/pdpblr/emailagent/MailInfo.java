package org.pdpblr.emailagent;

import java.util.List;

public class MailInfo {
	private String toAddress;
	private String fromAddress;
	private String body;
	private List<String> attachments;
	
	public MailInfo(String toAddress, String fromAddress, String body) {
		this.toAddress = toAddress;
		this.fromAddress = fromAddress;
		this.body = body;
	}
	
	public MailInfo(String toAddress, String fromAddress, String body, List<String> attachments) {
		this(toAddress, fromAddress, body);
		this.attachments = attachments;
	}
	
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public List<String> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}

}
