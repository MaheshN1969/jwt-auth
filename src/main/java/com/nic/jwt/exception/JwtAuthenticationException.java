package com.nic.jwt.exception;

import javax.naming.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException{
	
	public JwtAuthenticationException(String msg, Throwable cause) {
        super(msg);
    }

}
