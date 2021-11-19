package com.topicos.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
//
//@Component
//public class JwtTokenUtil implements Serializable {
//
//  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60L;
//
//  public static final long JWT_TOKEN_MAIL_VALIDITY = 7 * 24 * 60 * 60L;
//
//  private static final long serialVersionUID = -2550185165626007488L;
//
//  @Value("${spring.jwt.secret}")
//  private String secret;
//
//  public String getUsernameFromToken(String token) {
//    return getClaimFromToken(token, Claims::getSubject);
//  }
//
//  public Date getExpirationDateFromToken(String token) {
//    return getClaimFromToken(token, Claims::getExpiration);
//  }
//
//  public Boolean getAdminFromToken(String token) {
//    final Claims claims = getAllClaimsFromToken(token);
//    return (Boolean) claims.get("admin");
//  }
//
//  public Long getCompanyFromToken(String token) {
//    final Claims claims = getAllClaimsFromToken(token);
//    return (Long) claims.get("company");
//  }
//
//  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//    final Claims claims = getAllClaimsFromToken(token);
//    return claimsResolver.apply(claims);
//  }
//
//  private Claims getAllClaimsFromToken(String token) {
//    return Jwts
//        .parser()
//        .setSigningKey(secret)
//        .parseClaimsJws(token)
//        .getBody();
//  }
//
//  public Boolean isTokenExpired(String token) {
//    final Date expiration = getExpirationDateFromToken(token);
//    return expiration.before(new Date());
//  }
//
//  public String generateToken(UserDetails userDetails, Boolean admin, Long company) {
//    Map<String, Object> claims = new HashMap<>();
//    claims.put("admin", admin);
//    claims.put("company", company);
//
//    return doGenerateToken(claims, userDetails.getUsername());
//  }
//
//  /**
//   * while creating the token - 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID 2. Sign the JWT using the
//   HS512
//   * algorithm and secret key. 3. According to JWS Compact Serialization(https://tools.ietf
//   * .org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//   * compaction of the JWT to a URL-safe string
//   *
//   * @param claims
//   * @param subject
//   * @return token generate
//   */
//  private String doGenerateToken(Map<String, Object> claims, String subject) {
//
//    return Jwts
//        .builder()
//        .setClaims(claims)
//        .setSubject(subject)
//        .setIssuedAt(new Date(System.currentTimeMillis()))
//        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//        .signWith(SignatureAlgorithm.HS512, secret)
//        .compact();
//  }
//
//  public Boolean validateToken(String token, UserDetails userDetails) {
//    final String username = getUsernameFromToken(token);
//    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//  }
//
//}

@Component
public class JwtTokenUtil implements Serializable {

  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

  private static final long serialVersionUID = -2550185165626007488L;

  @Value("${spring.jwt.secret}")
  private String secret;

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts
        .parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .getBody();
  }

  public Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  public String generateToken(UserDetails userDetails, Boolean admin, Long company) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("admin", admin);
    claims.put("company", company);
    return doGenerateToken(claims, userDetails.getUsername());
  }

  /**
   * while creating the token - 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID 2. Sign the JWT using the HS512
   * algorithm and secret key. 3. According to JWS Compact Serialization(https://tools.ietf
   * .org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
   * compaction of the JWT to a URL-safe string
   *
   * @param claims
   * @param subject
   * @return token generate
   */
  private String doGenerateToken(Map<String, Object> claims, String subject) {

    return Jwts
        .builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public boolean getAdminFromToken(String token) {
    final Claims claims = getAllClaimsFromToken(token);
    return (boolean) claims.get("admin");
  }

  public Long getCompanyFromToken(String token) {
    final Claims claims = getAllClaimsFromToken(token);
    return Long.valueOf(claims
        .get("company")
        .toString());
  }

}