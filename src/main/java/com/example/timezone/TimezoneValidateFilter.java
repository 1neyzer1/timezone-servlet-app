package com.example.timezone;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TimeZone;

@WebFilter(value = "/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String timezone = req.getParameter("timezone");

        if (timezone != null && !timezone.trim().isEmpty()) {
            String tzStr = timezone.replace(" ", "+");
            if (tzStr.startsWith("UTC")) {
                tzStr = tzStr.replaceFirst("UTC", "GMT");
            }

            TimeZone tz = TimeZone.getTimeZone(tzStr);
            
            // Validation: if getTimeZone returns "GMT", it means parsing failed (unless input was actually GMT)
            if ("GMT".equals(tz.getID()) && !tzStr.equals("GMT")) {
                HttpServletResponse resp = (HttpServletResponse) response;
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().write("<html><body><h1>Invalid timezone</h1></body></html>");
                return;
            }
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
