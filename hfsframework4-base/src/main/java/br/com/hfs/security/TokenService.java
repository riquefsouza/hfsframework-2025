package br.com.hfs.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Password;

@Service
public class TokenService {
	
	@Value("${hefesto.jwt.expiration}")
	private String expiration;
	
	@Value("${hefesto.jwt.secret}")
	private String secret;
	
	public String gerarToken(Authentication authentication) {
		HfsUserDetails user = (HfsUserDetails) authentication.getPrincipal();
		Date today = new Date();
		Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));
	
		Password key = Keys.password(this.secret.toCharArray());
		//SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secret));		
		
		return Jwts.builder()
				.issuer("hfsframework4-base")
				.subject(user.getId().toString())
				.issuedAt(today)
				.expiration(expirationDate)
				.signWith(key, Jwts.SIG.HS256)
				.claims(user.getClaims())
				.compact();
	}

	public boolean isValidToken(String token) {
		Password key = Keys.password(this.secret.toCharArray());
		
		try {
			//Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);			
					
			Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getIdUser(String token) {
		Password key = Keys.password(this.secret.toCharArray());
		
		//Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		//return Long.parseLong(claims.getSubject());
		
		Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
		
		return Long.parseLong(claims.get("id").toString());
	}

}
