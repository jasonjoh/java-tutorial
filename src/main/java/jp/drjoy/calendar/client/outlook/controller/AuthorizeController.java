package jp.drjoy.calendar.client.outlook.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.drjoy.calendar.client.outlook.auth.AuthHelper;
import jp.drjoy.calendar.client.outlook.auth.IdToken;
import jp.drjoy.calendar.client.outlook.auth.TokenResponse;
import jp.drjoy.calendar.client.outlook.service.OutlookService;
import jp.drjoy.calendar.client.outlook.service.OutlookServiceBuilder;
import jp.drjoy.calendar.client.outlook.service.OutlookUser;

@Controller
public class AuthorizeController {

    @RequestMapping(value = "/authorize", method = RequestMethod.POST)
    public String authorize(
            @RequestParam("code") String code,
            @RequestParam("id_token") String idToken,
            @RequestParam("state") UUID state,
            HttpServletRequest request) {

        HttpSession session = request.getSession();

        // Make sure that the state query parameter returned matches
        // the expected state
        IdToken idTokenObj = IdToken.parseEncodedToken(idToken);
        if (idTokenObj != null) {
            TokenResponse tokenResponse =
                    AuthHelper.getTokenFromAuthCode(code, idTokenObj.getTenantId());
            session.setAttribute("tokens", tokenResponse);
            session.setAttribute("userConnected", true);
            session.setAttribute("userName", idTokenObj.getName());
            session.setAttribute("userTenantId", idTokenObj.getTenantId());
            // Get user info
            OutlookService outlookService =
                    OutlookServiceBuilder.getOutlookService(tokenResponse.getAccessToken(), null);
            OutlookUser user;
            try {
                user = outlookService.getCurrentUser().execute().body();
                session.setAttribute("userEmail", user.getMail());
            } catch (IOException e) {
                session.setAttribute("error", e.getMessage());
            }
        } else {
            session.setAttribute("error", "ID token failed validation.");
        }

        return "redirect:/mail.html";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/index.html";
    }
}
