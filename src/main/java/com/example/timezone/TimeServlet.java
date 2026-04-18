package com.example.timezone;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JavaxServletWebApplication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private JavaxServletWebApplication application;

    @Override
    public void init() throws ServletException {
        application = JavaxServletWebApplication.buildApplication(getServletContext());
        
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("UTF-8");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String timezoneParam = req.getParameter("timezone");
        String timezone = null;

        if (timezoneParam != null && !timezoneParam.trim().isEmpty()) {
            timezone = timezoneParam.replace(" ", "+");
            String encodedCookieValue = URLEncoder.encode(timezone, StandardCharsets.UTF_8.toString());
            Cookie cookie = new Cookie("lastTimezone", encodedCookieValue);
            cookie.setMaxAge(60 * 60 * 24 * 365); // 1 year
            cookie.setPath("/"); // Allow cookie across the app
            resp.addCookie(cookie);
        } else {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("lastTimezone".equals(c.getName())) {
                        timezone = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8.toString());
                        break;
                    }
                }
            }
        }

        if (timezone == null || timezone.trim().isEmpty()) {
            timezone = "UTC";
        }
        
        String parseableTz = timezone.startsWith("UTC") ? timezone.replaceFirst("UTC", "GMT") : timezone;
        TimeZone tz = TimeZone.getTimeZone(parseableTz);
        
        LocalDateTime now = LocalDateTime.now(tz.toZoneId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter) + " " + timezone;

        resp.setContentType("text/html;charset=UTF-8");
        
        IWebExchange webExchange = application.buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);
        context.setVariable("time", formattedTime);

        templateEngine.process("time", context, resp.getWriter());
    }
}
