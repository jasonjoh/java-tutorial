package com.outlook.dev.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.outlook.dev.auth.AuthHelper;
import com.outlook.dev.auth.TokenResponse;
import com.outlook.dev.service.Message;
import com.outlook.dev.service.OutlookService;
import com.outlook.dev.service.OutlookServiceBuilder;
import com.outlook.dev.service.PagedResult;

@Controller
public class MailController {

	@RequestMapping("/mail")
	public String mail(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		HttpSession session = request.getSession();
		TokenResponse tokens = (TokenResponse)session.getAttribute("tokens");
		if (tokens == null) {
			// No tokens in session, user needs to sign in
			redirectAttributes.addFlashAttribute("error", "Please sign in to continue.");
			return "redirect:/index.html";
		}
		
		String tenantId = (String)session.getAttribute("userTenantId");
		
		tokens = AuthHelper.ensureTokens(tokens, tenantId);
		
		String email = (String)session.getAttribute("userEmail");
		
		OutlookService outlookService = OutlookServiceBuilder.getOutlookService(tokens.getAccessToken(), email);
		
		// Retrieve messages from the inbox
		String folder = "inbox";
		// Sort by time received in descending order
		String sort = "receivedDateTime DESC";
		// Only return the properties we care about
		String properties = "receivedDateTime,from,isRead,subject,bodyPreview";
		// Return at most 10 messages
		Integer maxResults = 10;
		
		try {
			PagedResult<Message> messages = outlookService.getMessages(
					folder, sort, properties, maxResults)
					.execute().body();
			model.addAttribute("messages", messages.getValue());
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/index.html";
		}
		
		return "mail";
	}
}
