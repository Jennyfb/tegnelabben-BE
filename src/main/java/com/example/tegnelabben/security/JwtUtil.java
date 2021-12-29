package com.example.tegnelabben.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Class for administrating and validating jwt tokens.
 */
@Service
public class JwtUtil {
  @Value("${api.secret}")
  private String SECRET_KEY;

  /**
   * Method for extracting username from token
   * @param token the token the username will be extracted from
   * @return username
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Method for extracting expiration time from token
   * @param token the token that expiration will be extracted from
   * @return the time the token expires
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Method for extracting given claim from a token
   * @param token tokem to extract claim from
   * @param claimsResolver
   * @param <T>
   * @return claim
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Method for extracting all claims from a token
   * @param token the token to extract claims from
   * @return all claims
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  /**
   * Method to check if token has expired
   * @param token the token that will be checked
   * @return if token is expired
   */
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Method for generating token with userDetails
   * @param userDetails the user the token will belong to
   * @return the newly generated token
   */
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
  }

  /**
   * Method for validating token
   * @param token token to be checked if is valid
   * @param userDetails the reference to check if is valid
   * @return if is valid token
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
