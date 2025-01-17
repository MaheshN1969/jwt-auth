package com.nic.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nic.jwt.entity.UserMasterEntity;
import com.nic.jwt.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserMasterEntity user = userRepo.findByEmailId(username);
		
		if(user == null)
		{
			System.out.println("User Not Found");
			throw new UsernameNotFoundException("User does not exist!!!!");
		}
		
		return new CustomUserDetails(user);
	}

}
