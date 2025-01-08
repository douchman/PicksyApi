package com.buck.vsplay.global.security.jwt;

import com.buck.vsplay.global.security.jwt.exception.JwtException;
import com.buck.vsplay.global.security.jwt.exception.JwtExceptionCode;
import com.buck.vsplay.global.security.user.CustomUserDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtService{

    @Value("${jwt.key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    public String generateAccessToken(CustomUserDetail customUserDetail) {
        Key key = getKeyFromSecretKey(secretKey);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customUserDetail.getId());

        List<String> roles = customUserDetail.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .setSubject("accessToken")
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long extractUserId(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKeyFromSecretKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);
    }

    @SuppressWarnings("unchecked")
    public List<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKeyFromSecretKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean isTokenExpired(String token){
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    public boolean validateToken(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 발급자 검증
            if(!issuer.equals(claims.getIssuer())){
                throw new JwtException(JwtExceptionCode.INVALID_ISSUER);
            }
            return true;
        }catch (ExpiredJwtException e){
            throw new JwtException(JwtExceptionCode.EXPIRED_TOKEN);
        }catch (UnsupportedJwtException e){
            throw new JwtException(JwtExceptionCode.UNSUPPORTED_TOKEN);
        }catch (MalformedJwtException e){
            throw new JwtException(JwtExceptionCode.MALFORMED_TOKEN);
        }catch (SignatureException e){
            throw new JwtException(JwtExceptionCode.INVALID_SIGNATURE);
        }catch (IllegalArgumentException e){
            throw new JwtException(JwtExceptionCode.ILLEGAL_ARGUMENT);
        }
    }

    private Key getKeyFromSecretKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }

}
