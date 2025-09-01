package com.dailytracker.filter;

import com.dailytracker.model.User;

// Change these imports from javax.servlet to jakarta.servlet
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/addTask", "/viewTasks", "/markTask", "/productivity", "/logout"})
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false); // Do not create a new session if one doesn't exist

        boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);

        // Allow access to login, register, CSS/JS resources, etc. without being logged in
        String loginURI = req.getContextPath() + "/login";
        String registerURI = req.getContextPath() + "/register";
        String cssURI = req.getContextPath() + "/css/"; // Static resources
        String jsURI = req.getContextPath() + "/js/";   // Static resources

        boolean isLoginRequest = req.getRequestURI().equals(loginURI);
        boolean isRegisterRequest = req.getRequestURI().equals(registerURI);
        boolean isCssResource = req.getRequestURI().startsWith(cssURI);
        boolean isJsResource = req.getRequestURI().startsWith(jsURI);

        if (isLoggedIn || isLoginRequest || isRegisterRequest || isCssResource || isJsResource) {
            // User is logged in OR it's a login/register request OR it's a static resource
            // Continue with the request
            chain.doFilter(request, response);
        } else {
            // User is not logged in and trying to access a protected resource
            // Redirect to the login page
            res.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }
}