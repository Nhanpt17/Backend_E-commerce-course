package com.human.graduateproject.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    public static final String SECRET_KEY="mysecretkeymysecretkeymysecretkey1234mysecretkeymysecretkeymysecretkey1234"; //At least 256 bits
//    private final long ACCESS_TOKEN_EXPIRATION = 30 * 60 * 1000;//30 MINUTES;
//    private final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7 ngay
    private final long ACCESS_TOKEN_EXPIRATION = 3 * 60 * 1000;//3 MINUTES;
    private final long REFRESH_TOKEN_EXPIRATION = 6 * 60 * 1000; // 6minutes


    private SecretKey getSigningKey(){
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String generateAccessToken(String username){
        Map<String,Object> claims = new HashMap<>();

        return createToken(claims,username,ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(String username){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,username,REFRESH_TOKEN_EXPIRATION);
    }



    public String createToken(Map<String,Object> claims, String username, long expirationTime){
        return Jwts.builder().claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and()
                .signWith(getSigningKey()) // correct signing method
                .compact();
    }






    //extract all claims from jwt token
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //extract a specific claim from jwt token
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //extract the username from jwt token
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    // extract the expiration form token
    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    // check if token is expired
    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //validate token by checking username and expiration
    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
