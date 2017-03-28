package com.outlook.dev.service;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
	private String id;
	private Date receivedDateTime;
	private Recipient from;
	private Boolean isRead;
	private String subject;
	private String bodyPreview;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getReceivedDateTime() {
		return receivedDateTime;
	}
	public void setReceivedDateTime(Date receivedDateTime) {
		this.receivedDateTime = receivedDateTime;
	}
	public Recipient getFrom() {
		return from;
	}
	public void setFrom(Recipient from) {
		this.from = from;
	}
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBodyPreview() {
		return bodyPreview;
	}
	public void setBodyPreview(String bodyPreview) {
		this.bodyPreview = bodyPreview;
	}
}
