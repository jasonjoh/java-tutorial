package jp.drjoy.calendar.client.outlook.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jp.drjoy.calendar.client.outlook.Outlook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.drjoy.calendar.client.outlook.auth.AuthHelper;

@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        Outlook outlook = new Outlook();
        model.addAttribute("loginUrl", outlook.getAuthUrl());
        // Name of a definition in WEB-INF/defs/pages.xml
        return "index";
    }
}
