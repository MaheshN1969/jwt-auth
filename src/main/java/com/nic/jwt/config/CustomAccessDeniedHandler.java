package com.nic.jwt.config;

import java.io.IOException;
import java.sql.Timestamp;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nic.jwt.entity.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		
		response.setContentType("application/json");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		
		ErrorResponse error = new ErrorResponse();
		error.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		error.setMessage("Access to protected resource denied.");
		error.setPath(request.getServletPath());
		error.setTime(new Timestamp(System.currentTimeMillis()));
		
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.writeValue(response.getOutputStream(), error);
		
	}

}
