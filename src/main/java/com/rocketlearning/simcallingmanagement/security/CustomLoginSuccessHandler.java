package com.rocketlearning.simcallingmanagement.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.rocketlearning.simcallingmanagement.service.ActivityLogService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private ActivityLogService activityLogService;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
    	
    	activityLogService.saveLog(
    	        authentication.getName(),
    	        "Authentication",
    	        "Login",
    	        "User logged into the system successfully.");

        var authorities =
                AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (authorities.contains("ROLE_ADMIN")) {

            response.sendRedirect("/dashboard");

        } else if (authorities.contains("ROLE_EMPLOYEE")) {

            response.sendRedirect("/employee/dashboard");

        } else {

            response.sendRedirect("/login");

        }

    }

}