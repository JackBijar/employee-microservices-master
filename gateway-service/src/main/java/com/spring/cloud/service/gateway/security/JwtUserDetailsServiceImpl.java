package com.spring.cloud.service.gateway.security;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    private static final Logger LOGGER = Logger.getLogger(JwtUserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
    {
        User user = userRepository.findByUsername(username);

        if (user == null) 
        {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        else 
        {
            return JwtUserFactory.create(user);
        }        
    }

    public User saveUserInfo(User modelUser)
    {
    	User user = userRepository.findByUsername(modelUser.getEmail());				//--------->Check User Already Exist OR Not<--------
    	
    	if (user == null) 
        {
    		LOGGER.info("This User is a New User");
    		
    		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();				//---------> Password Encryption with Save <--------
    		modelUser.setPassword(passwordEncoder.encode(modelUser.getPassword()));     		
    		
    		modelUser.setUsername(modelUser.getEmail()); 								//---------> Email ID SET as UserName <----------- 
    		    	   
    		modelUser.setEnabled(true);													//---------> Enable Default User Activation <--------
    		
    		Date date = new Date();  
    		    		
    		modelUser.setLastPasswordResetDate(date);									//---------> Password Resate Date "2017-09-12 06:01:01" <--------					
    		
    		List<Authority> authorities = new ArrayList<>();    		
    		Authority authority = new Authority();										
    		
    		authority.setId((long) 3);													//---------> Set Default Authority "ROLE_USER" <-----------									
    		
    		List<User> users = new ArrayList<>();    		
    		modelUser = userRepository.save(modelUser);									//----------> Save User and Get User ID <--------------- 
    		
    		users.add(modelUser);
    		
    		authority.setUsers(users);
    		
    		authorities.add(authority);
    		
    		modelUser.setAuthorities(authorities);    		
    		
    		return userRepository.save(modelUser);									//----------> Update User Model <--------------- 
        }
    	else
    		LOGGER.info("This User already exist : "+user);
    	
    	return user;
    }
}
