package com.nic.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nic.jwt.config.CustomUserDetails;
import com.nic.jwt.config.JwtUtils;
import com.nic.jwt.entity.ActiveToken;
import com.nic.jwt.entity.ErrorResponse;
import com.nic.jwt.entity.JwtRequest;
import com.nic.jwt.entity.JwtResponse;
import com.nic.jwt.repository.ActiveTokenRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ActiveTokenRepository activeTokenRepo;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody JwtRequest request)
	{
		System.out.println("authenticateUser() of Controller.");
		JwtResponse response ;
		Authentication authentication;
		String jwtToken = "";
		try
		{
			
			authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.getUsername(),
																										 request.getPassword()));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			
			jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
			
			List<String> role = userDetails.getAuthorities().stream()
	                 						.map(item -> item.getAuthority())
	                 						.collect(Collectors.toList());
			response = new JwtResponse();
			response.setRole(role);
			response.setUsername(userDetails.getUsername());
			response.setToken(jwtToken);
			
			
			
//			return ResponseEntity.ok(response);
			
		}
		catch(AuthenticationException e)
		{
			ErrorResponse error = new ErrorResponse();
			error.setMessage("Invalid email-id or password");
			error.setStatusCode(HttpStatus.BAD_REQUEST.value());
			error.setTime(new Timestamp(System.currentTimeMillis()));
			error.setPath("/api/v1/login");
			
			return ResponseEntity.badRequest().body(error);
		}
		
		ActiveToken tokenEntity = new ActiveToken();
		tokenEntity.setActiveFlag(true);
		tokenEntity.setEmailId(request.getUsername());
		tokenEntity.setToken(jwtToken);
		
		activeTokenRepo.save(tokenEntity);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String token)
	{
		System.out.println("Token : "+token);
		
		ActiveToken tokenEntity = activeTokenRepo.findByToken(token.substring(7));
		
		tokenEntity.setActiveFlag(false);
		
		activeTokenRepo.save(tokenEntity);
		
		Map<String,String> response = new HashMap();
		
		response.put("message", "User logout successfully.");
		response.put("time", new Timestamp(System.currentTimeMillis()).toString());
		
		
		
		return ResponseEntity.ok(response);
	}
	
}
