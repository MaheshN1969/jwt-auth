package com.nic.jwt.config;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.nic.jwt.entity.ActiveToken;
import com.nic.jwt.exception.InvalidTokenException;
import com.nic.jwt.exception.JwtAuthenticationException;
import com.nic.jwt.repository.ActiveTokenRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtils {
	
	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${spring.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	@Autowired
	private ActiveTokenRepository activeTokenRepo;

	
	public String getJwtFromHeader(HttpServletRequest request)
	{
		String token = request.getHeader("Authorization");
		
		if(token != null && token.startsWith("Bearer ") )
		{
			return token.substring(7);
		}
		
		return null;
	}

	public String generateTokenFromUsername(UserDetails userDetails)
	{
		String username = userDetails.getUsername();
		
		return Jwts.builder()
				   .subject(username)
				   .issuedAt(new Date())
				   .expiration(new Date((new Date()).getTime() + jwtExpirationMs) )
				   .signWith(key())
				   .compact();
	}
	
	
	private Key key()
	{
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	public String getUsernameFromJwtToken(String token)
	{
		return Jwts.parser()
				   .verifyWith((SecretKey) key())
				   .build()
				   .parseSignedClaims(token)
				   .getPayload()
				   .getSubject();
	}
	
	public boolean validateJwtToken(String token) throws Exception
	{
		try 
		{
			ActiveToken activeToken = activeTokenRepo.findByToken(token);
			
			if(activeToken.isActiveFlag() == false)
			{
				throw new InvalidTokenException("Invalid Token Exception");
			}
			
			Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token);
			return true;
		}
		catch(MalformedJwtException e)
		{
			System.out.println("Invalid JWT Token !!!: "+e.getMessage());
			throw new MalformedJwtException("MalformedJWTException");
			
		}
		catch(ExpiredJwtException e)
		{
			System.out.println("JWT Token Expired !!! : "+e.getMessage());
//			throw new JwtAuthenticationException("ExpiredJwtException",e);
			throw new ExpiredJwtException(Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getHeader() ,Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getBody(),"Expired JWT Token.");
		}
		catch(UnsupportedJwtException e)
		{
			System.out.println("JWT Token is Unsupported !!!: "+e.getMessage());
			throw new UnsupportedJwtException("Unsupported JWT Exception");
		}
		catch(IllegalArgumentException e)
		{
			System.out.println("JWT Claim String is empty !!! : "+e.getMessage());
		}
		
		return false;
	}
}
