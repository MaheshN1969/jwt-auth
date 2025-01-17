package com.nic.jwt.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nic.jwt.exception.InvalidTokenException;
import com.nic.jwt.exception.JwtAuthenticationException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private CustomUserDetailService userDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("Inside doFilterInternal() of AuthTokenFilter");
		
		try 
		{
			String jwt = parseJwt(request);
			
			if(jwt != null && jwtUtils.validateJwtToken(jwt))
			{
				String username = jwtUtils.getUsernameFromJwtToken(jwt);
				
				UserDetails userDetails = userDetailService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( 
																												userDetails,
																												null,
																												userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			
			}
		}
		catch(ExpiredJwtException e)
		{
			System.out.println("AuthTokenFilter Exception");
			
			request.setAttribute("exception", "ExpiredJwtException");
			request.setAttribute("token", parseJwt(request));
			
		}
		catch(MalformedJwtException e)
		{
			System.out.println("MalformedJwtException Exception");
			
			request.setAttribute("exception", "MalformedJwtException");	
		}
		catch(UnsupportedJwtException e)
		{
			System.out.println("UnsupportedJwtException Exception");
			
			request.setAttribute("exception", "UnsupportedJwtException");
		}
		catch(InvalidTokenException e)
		{
			System.out.println("InvalidTokenException Exception");
			
			request.setAttribute("exception", "InvalidTokenException");
		}
		catch(Exception e)
		{
			System.out.println("Authentication failed !!! : "+e.getMessage());
			
			request.setAttribute("exception", "UnknownException");
			
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	private String parseJwt(HttpServletRequest request)
	{
		String jwt = jwtUtils.getJwtFromHeader(request);
		return jwt;
	}

}
