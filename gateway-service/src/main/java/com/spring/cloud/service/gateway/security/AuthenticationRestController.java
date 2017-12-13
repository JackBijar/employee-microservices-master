package com.spring.cloud.service.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtUserDetailsServiceImpl jwtUserDetailsServiceImpl;
    
    @Autowired
    private User modelUser;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException 
    {
    	System.out.println("Received Value =:= "+authenticationRequest);
        // Perform the security
    	
    	System.out.println("=======>> "+device.isMobile());
    	
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        
        final String token = jwtTokenUtil.generateToken(userDetails, device);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) 
    {
    	System.out.println("================================IN REFRESH TOKEN==================");
    	
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) 
        {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } 
        else 
        {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
 //-------------------------------------------------------------------------------------------------------------NEW ADDED CODE--------------------------------
    
    @RequestMapping(value = "/auth/signUp", method = RequestMethod.POST)
    public String newUserAuthentication(@ModelAttribute ("dataString") User user, BindingResult result) throws AuthenticationException 
    {
        // Perform the security
    	
    	System.out.println("===============*====*==================!!"+user.toString());
    	
    	modelUser = jwtUserDetailsServiceImpl.saveUserInfo(user);
        
        return modelUser.toString();
    }    
    
    @RequestMapping(value = "/auth/signIn", method = RequestMethod.POST)
    public String createAuthToken(@ModelAttribute ("dataString") JwtAuthenticationRequest authenticationRequest, Device device, BindingResult result) throws AuthenticationException 
    {
    	System.out.println("Received Value =:= "+authenticationRequest);
    	System.out.println("Received Device =:= "+device.isNormal());  	
    	
    	
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        
        final String token = jwtTokenUtil.generateToken(userDetails, device);
        
        // Return the token
        return token;
    }    
}
