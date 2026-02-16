package com.ffpalu.concessionaria.security;

import com.ffpalu.concessionaria.entity.Credential;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long jwtExpirationInMs;


	public String generateToken(Credential credential) {
		Map<String, Object> extraFieldClaims = new HashMap<>();

		extraFieldClaims.put("role", credential.getRole());
		extraFieldClaims.put("userId", credential.getUser().getId());

		return Jwts.builder()
						.claims(extraFieldClaims)
						.subject(credential.getUsername())
						.issuedAt(new Date(System.currentTimeMillis()))
						.expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
						.signWith(getSignInKey())
						.compact();

	}


	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsernameFromToken(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpirationDateFromToken(token).before(new Date());
	}


	public String extractUsernameFromToken(String token) {
		return extractFieldFromClaimToken(token, Claims::getSubject);
	}

	public Date extractExpirationDateFromToken(String token) {
		return extractFieldFromClaimToken(token, Claims::getExpiration);
	}

	public String extractRoleFromToken(String token) {
		return extractFieldFromClaimToken(token, claims -> claims.get("role").toString());
	}

	public <Field> Field extractFieldFromClaimToken(String token, Function<Claims, Field> claimsGenericsFieldFunction) {
		Claims claims = extractAllClaimsFromToken(token);
		return claimsGenericsFieldFunction.apply(claims);
	}

	public Claims extractAllClaimsFromToken(String token) {
		return Jwts.parser()
						.verifyWith(getSignInKey())
						.build()
						.parseSignedClaims(token)
						.getPayload();
	}

	private SecretKey getSignInKey() {
		byte[] keyBytesDecoded = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytesDecoded);
	}

}
