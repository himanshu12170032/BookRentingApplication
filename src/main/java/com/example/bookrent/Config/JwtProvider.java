package com.example.bookrent.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {
  SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
  public String generateToken(Authentication authentication){
      String jwt = Jwts.builder().setIssuedAt(new Date())
              .setExpiration(new Date(new Date().getTime()+86400000L))
              .claim("email", authentication.getName())
              .claim("authorities", populateAuthorities(authentication.getAuthorities()))
              .signWith(key)
              .compact();
      return jwt;
  }
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesSet = new HashSet<>();
        for(GrantedAuthority authority: authorities) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }

    public String getEmailFromToken(String jwt){
      jwt = jwt.substring(7);

      Claims claims = Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(jwt)
              .getBody();
      String email = String.valueOf(claims.get("email"));
      return email;
  }
}
