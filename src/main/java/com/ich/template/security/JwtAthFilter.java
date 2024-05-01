package com.ich.template.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAthFilter extends OncePerRequestFilter {
	
	@Autowired
	JwtService jwtService;
	//private final UserDetailsService userDetailsServices;
	
	@Autowired
	//@Qualifier("userDetailsService")
	UserDetailsService userDetailsServices;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("AUTHORIZATION");
		final String userEmail;
		final String jwt;
		//si no tiene autorizacion o simplemente no es la correcta 'Bearer' (la de autorización) 
		//no queremos seguir ejecutando, asi que pasamos al siguiente filtro
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			 filterChain.doFilter(request, response);// <- pasamos al siguiente filtro
			 return;
		}
		jwt = authHeader.substring(7);//Obtener mi token -> "Bearer:token"
		userEmail = jwtService.extractUsername(jwt);// extraer el userEmail del jwt
		//si existe el mail en el jwt y checkear si ya esta autentificado
		if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			//si el usuario no esta conectado 'SecurityContextHolder.getContext().getAuthentication() == null'
			UserDetails userDetails =  userDetailsServices.loadUserByUsername(userEmail);
		   /*   var isTokenValid = tokenRepository.findByToken(jwt)
		              .map(t -> !t.isExpired() && !t.isRevoked())
		              .orElse(false);*/
			if (jwtService.isTokenValid(jwt, userDetails)/* && isTokenValid*/) {
		        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
		            userDetails,
		            null,
		            userDetails.getAuthorities()
		        );
		        authToken.setDetails(
		            new WebAuthenticationDetailsSource().buildDetails(request)
		        );
		        SecurityContextHolder.getContext().setAuthentication(authToken);
		      }
		}
		filterChain.doFilter(request, response);//darle el control al siguiente filtro
	}

}
