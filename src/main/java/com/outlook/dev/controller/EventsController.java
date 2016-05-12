package com.outlook.dev.controller;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.outlook.dev.auth.TokenResponse;
import com.outlook.dev.service.Event;
import com.outlook.dev.service.OutlookService;
import com.outlook.dev.service.PagedResult;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Controller
public class EventsController {

	@RequestMapping("/events")
	public String mail(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
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
		
		OutlookService outlookService = getOutlookService(tokens.getAccessToken(), email);
		
		// Sort by start time in descending order
		String sort = "Start/DateTime DESC";
		// Only return the properties we care about
		String properties = "Organizer,Subject,Start,End";
		// Return at most 10 messages
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
	
	private OutlookService getOutlookService(String accessToken, String userEmail) {
		// Create a request interceptor to add headers that belong on
		// every request
		Interceptor requestInterceptor = new Interceptor() {
			@Override
			public Response intercept(Interceptor.Chain chain) throws IOException {
				Request original = chain.request();
				
				Request request = original.newBuilder()
						.header("User-Agent", "java-tutorial")
						.header("client-request-id", UUID.randomUUID().toString())
						.header("return-client-request-id", "true")
						.header("X-AnchorMailbox", userEmail)
						.header("Authorization", String.format("Bearer %s", accessToken))
						.method(original.method(), original.body())
						.build();
				
				return chain.proceed(request);
			}
		};
				
		// Create a logging interceptor to log request and responses
		HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
		loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		
		OkHttpClient client = new OkHttpClient.Builder()
				.addInterceptor(requestInterceptor)
				.addInterceptor(loggingInterceptor)
				.build();
		
		// Create and configure the Retrofit object
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://outlook.office.com")
				.client(client)
				.addConverterFactory(JacksonConverterFactory.create())
				.build();
		
		// Generate the token service
		return retrofit.create(OutlookService.class);
	}
}
