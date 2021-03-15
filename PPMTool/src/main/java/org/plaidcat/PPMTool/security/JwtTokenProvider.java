package org.plaidcat.PPMTool.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.plaidcat.PPMTool.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import static org.plaidcat.PPMTool.security.SecurityConstants.*;

@Component
public class JwtTokenProvider {
	
	Logger log = Logger.getLogger(JwtTokenProvider.class);
	
	//generate a token
	public String generateToken(Authentication auth) {
		User user = (User)auth.getPrincipal();
		Date now = new Date(System.currentTimeMillis());
		Date expireDate = new Date(now.getTime() + EXPIRATION_TIME);
		
		String userId = Long.toString(user.getId());
		
		Map<String,Object> claims = new HashMap<>();
		claims.put("id", Long.toString(user.getId()));
		claims.put("username", user.getUsername());
		claims.put("fullName", user.getFullName());
				
		return Jwts.builder()
				.setSubject(userId)
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();		
	}
	
	// validate a token	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			return true;			
		} catch (SignatureException ex) {			
			log.info("Invalid JWT Signature");
		} catch (MalformedJwtException ex) {
			log.info("Invalid JWT Token");
		} catch (ExpiredJwtException ex) {
			log.info("Token is expired");
		} catch (UnsupportedJwtException ex) {
			log.info("Unsupported JWT Exception");
		} catch (IllegalArgumentException ex) {
			log.info("JWT Claims string is empty");
		}
		return false;
	}
	
	//get user id from token
	public Long getUserIdFromJWT(String token) {		
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		return Long.parseLong((String)claims.get("id"));		
	}

}
