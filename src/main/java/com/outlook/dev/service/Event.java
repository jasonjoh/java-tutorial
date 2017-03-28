package com.outlook.dev.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
	private String id;
	private String subject;
	private Recipient organizer;
	private DateTimeTimeZone start;
	private DateTimeTimeZone end;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Recipient getOrganizer() {
		return organizer;
	}
	public void setOrganizer(Recipient organizer) {
		this.organizer = organizer;
	}
	public DateTimeTimeZone getStart() {
		return start;
	}
	public void setStart(DateTimeTimeZone start) {
		this.start = start;
	}
	public DateTimeTimeZone getEnd() {
		return end;
	}
	public void setEnd(DateTimeTimeZone end) {
		this.end = end;
	}
}
