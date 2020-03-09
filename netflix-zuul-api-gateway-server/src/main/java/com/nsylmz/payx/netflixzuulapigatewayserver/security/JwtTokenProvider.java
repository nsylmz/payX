package com.nsylmz.payx.netflixzuulapigatewayserver.security;

import com.nsylmz.payx.netflixzuulapigatewayserver.model.JwtToken;
import com.nsylmz.payx.netflixzuulapigatewayserver.repository.JwtTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    private static final String AUTH="auth";
    private static final String AUTHORIZATION="bearer";
    private String secretKey="secret-key";
    private long validityInMilliseconds = 3600000; // 1h

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTH,roles);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String token =  Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
        jwtTokenRepository.save(new JwtToken(token));
        //userServiceProxy.addToken(new TokenBean(token));
        return token;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION);
        //if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
        //    return bearerToken.substring(7, bearerToken.length());
        //}
        if (bearerToken != null ) {
            return bearerToken;
        }
        return null;
    }

    public boolean validateToken(String token) throws JwtException,IllegalArgumentException{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
    }
    public boolean isTokenPresentInDB (String token) {
        return jwtTokenRepository.existsById(token);
    }

    public UserDetails getUserDetails(String token) {
        List<String> roleList = getRoleList(token);
        return new PayXUserDetails(getUsername(token), roleList.toArray(new String[roleList.size()]));
    }
    
    @SuppressWarnings("unchecked")
	public List<String> getRoleList(String token) {
        return (List<String>) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(AUTH);
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetails(token);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}