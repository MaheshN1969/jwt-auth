package com.nic.jwt.entity;



import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private int statusCode;
	private String message;
	private Timestamp time;
	private String path;
	
	
	public ErrorResponse(int statusCode, String message, String path)
	{
		this.statusCode = statusCode;
		this.message = message;
		this.path = path;
		this.time = new Timestamp(System.currentTimeMillis());
	}
	
}
