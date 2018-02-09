package jp.drjoy.calendar.client.outlook.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.drjoy.calendar.client.outlook.auth.TokenResponse;
import jp.drjoy.calendar.client.outlook.service.Contact;
import jp.drjoy.calendar.client.outlook.service.OutlookService;
import jp.drjoy.calendar.client.outlook.service.OutlookServiceBuilder;
import jp.drjoy.calendar.client.outlook.service.PagedResult;

@Controller
public class ContactsController {
	@RequestMapping("/contacts")
	public String contacts(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
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
		
		// Sort by given name in ascending order (A-Z)
		String sort = "givenName ASC";
		// Only return the properties we care about
		String properties = "givenName,surname,companyName,emailAddresses";
		// Return at most 10 contacts
		Integer maxResults = 10;
		
		try {
			PagedResult<Contact> contacts = outlookService.getContacts(
					sort, properties, maxResults)
					.execute().body();
			model.addAttribute("contacts", contacts.getValue());
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/index.html";
		}
		
		return "contacts";
	}
}
