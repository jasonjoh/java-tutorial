package com.outlook.dev.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.outlook.dev.auth.TokenResponse;
import com.outlook.dev.service.Event;
import com.outlook.dev.service.OutlookService;
import com.outlook.dev.service.OutlookServiceBuilder;
import com.outlook.dev.service.PagedResult;

@Controller
public class EventsController {

	@RequestMapping("/events")
	public String events(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		HttpSession session = request.getSession();
		TokenResponse tokens = (TokenResponse)session.getAttribute("tokens");
		if (tokens == null) {
			// No tokens in session, user needs to sign in
			redirectAttributes.addFlashAttribute("error", "Please sign in to continue.");
			return "redirect:/index.html";
		}
		
		Date now = new Date();
		if (now.after(tokens.getExpirationTime())) {
			// Token expired
			// TODO: Use the refresh token to request a new token from the token endpoint
			// For now, just complain
			redirectAttributes.addFlashAttribute("error", "The access token has expired. Please logout and re-login.");
			return "redirect:/index.html";
		}
		
		String email = (String)session.getAttribute("userEmail");
		
		OutlookService outlookService = OutlookServiceBuilder.getOutlookService(tokens.getAccessToken(), email);
		
		// Sort by start time in descending order
		String sort = "start/DateTime DESC";
		// Only return the properties we care about
		String properties = "organizer,subject,start,end";
		// Return at most 10 events
		Integer maxResults = 10;
		
		try {
			PagedResult<Event> events = outlookService.getEvents(
					sort, properties, maxResults)
					.execute().body();
			model.addAttribute("events", events.getValue());
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/index.html";
		}
		
		return "events";
	}
}
