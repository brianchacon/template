package com.ich.template.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ich.template.dao.UserDAO;
import com.ich.template.dao.UserSessionDAO;
import com.ich.template.dto.AuthenticationRequest;
import com.ich.template.dto.AuthenticationResponse;
import com.ich.template.dto.RegisterRequest;
import com.ich.template.model.User;
import com.ich.template.model.UserSession;
import com.ich.template.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {
	  @Autowired
	  private UserDAO userDAO;
	 // private final TokenRepository tokenRepository;
	  @Autowired
	  private UserSessionDAO userSessionDAO;
	  @Autowired
	  private PasswordEncoder passwordEncoder;
	  @Autowired
	  private JwtService jwtService;
	  @Autowired
	  private AuthenticationManager authenticationManager;

	  //Llamada por el control
	  public AuthenticationResponse register(RegisterRequest request) {

	    var user = new User();
	    user.setFirstName(request.getFirstname());
	    user.setLastName(request.getLastname());
	    user.setEmail(request.getEmail());//<--debería validar que sea único, sino no funcionaran los tokens
	    user.setPassword(passwordEncoder.encode( request.getPassword() ));
	    user.setRole(request.getRole());
	    
	    var savedUser = userDAO.save(user);
	    
	    var jwtToken = jwtService.generateToken(user);
	    var refreshToken = jwtService.generateRefreshToken(user);
	    saveUserSession(savedUser, jwtToken,savedUser.getId());
	    
	    var authResponse = new AuthenticationResponse();
        authResponse.setAccessToken(jwtToken);
        authResponse.setRefreshToken(refreshToken);
	    
        return authResponse;
	  }

	  //Llamada por el control
	  public AuthenticationResponse authenticate(AuthenticationRequest request) {
 	  //la siguiente linea hace la autentificación y si algo esta mal tira excepcion
	    authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(
	            request.getEmail(),
	            request.getPassword()
	        )
	    );
	    var user = userDAO.findByEmail(request.getEmail())
	        .orElseThrow();
	    var jwtToken = jwtService.generateToken(user);
	    var refreshToken = jwtService.generateRefreshToken(user);
	   // revokeAllUserTokens(user);
	    saveUserSession(user, jwtToken,user.getId());
	    
	    var authResponse = new AuthenticationResponse();
        authResponse.setAccessToken(jwtToken);
        authResponse.setRefreshToken(refreshToken);
	    
        return authResponse;
	  }

		  private void saveUserSession(User user, String jwtToken,int sessionId) {
	
			var userSession = new UserSession();
	
			userSession.setId(sessionId);
		    userSession.setUser(user.getId());
		    userSession.setToken(jwtToken);
		    userSession.setSessionEnded(false);
		    userSession.setRevoked(false);
		    userSessionDAO.save(userSession);
		  }
	  
	  public void logOut(AuthenticationRequest request) {
		  var user = userDAO.findByEmail(request.getEmail()).orElseThrow();
		  revokeAllUserTokens( user);
	  }

		  private void revokeAllUserTokens(User user) {
	
		    var validUserTokens = userSessionDAO.findById(Long.valueOf( user.getId())).get();
	
		    validUserTokens.setSessionEnded(true);
		    validUserTokens.setRevoked(true);
	
		    userSessionDAO.save(validUserTokens);
		  }

	  //Llamada por el control
	  public void refreshToken(
	          HttpServletRequest request,
	          HttpServletResponse response
	  ) throws IOException {
	    final String authHeader = request.getHeader("AUTHORIZATION");
	    final String refreshToken;
	    final String userEmail;
	    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
	      return;
	    }
	    refreshToken = authHeader.substring(7);
	    userEmail = jwtService.extractUsername(refreshToken);
	    if (userEmail != null) {
	      var user = this.userDAO.findByEmail(userEmail)
	              .orElseThrow();
	      if (jwtService.isTokenValid(refreshToken, user)) {
	        var accessToken = jwtService.generateToken(user);
	        //revokeAllUserTokens(user);
	        saveUserSession(user, accessToken,user.getId());
	        
	        var authResponse = new AuthenticationResponse();
	        authResponse.setAccessToken(accessToken);
	        authResponse.setRefreshToken(refreshToken);
	        
	        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
	      }
	    }
	  }
}
