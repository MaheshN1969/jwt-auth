package com.nic.jwt.config;

import java.io.IOException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nic.jwt.entity.ActiveToken;
import com.nic.jwt.entity.ErrorResponse;
import com.nic.jwt.repository.ActiveTokenRepository;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Autowired
	ActiveTokenRepository activeTokenRepo;
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
//		ErrorResponse
		response.setContentType("application/json");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		
		String message="";
		int statusCode=-1;
		
//		System.out.println("Inside AUthEntryPoint. "+request.getAttribute("exception"));
		
		if(request.getAttribute("exception").equals("ExpiredJwtException") )
		{
			System.out.println("JWT Expired from AEP.");
			
			String token = request.getAttribute("token").toString();
			invalidateExpiredToken(token);
			
			message = "JWT Token expired.";
			statusCode = HttpStatus.UNAUTHORIZED.value();
		}
		else if(request.getAttribute("exception").equals("UnsupportedJwtException"))
		{
			message = "Unsupported JWT Exception occured";
			statusCode = HttpStatus.BAD_REQUEST.value();
		}
		else if(request.getAttribute("exception").equals("MalformedJwtException"))
		{
			message = "Malformed JWT Exception occured";
			statusCode = HttpStatus.BAD_REQUEST.value();
		}
		else if(request.getAttribute("exception").equals("UnknownException"))
		{
			message = "Unknown Exception occured";
			statusCode = HttpStatus.BAD_REQUEST.value();
		}
		else if(request.getAttribute("exception").equals("InvalidTokenException"))
		{
			message="Invalid Token ";
			statusCode = HttpStatus.UNAUTHORIZED.value();
		}
		
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(message);
		errorResponse.setStatusCode(statusCode);
		errorResponse.setPath(request.getServletPath());
		errorResponse.setTime(new Timestamp(System.currentTimeMillis()));
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), errorResponse);
		
		
	}
	
	public void invalidateExpiredToken(String token)
	{
		System.out.println("Invalidation expired token");
	
		ActiveToken activeToken = activeTokenRepo.findByToken(token);
		activeToken.setActiveFlag(false);
		activeTokenRepo.save(activeToken);
		
		System.out.println("Expired token invalidated successfully.");
	}

}
