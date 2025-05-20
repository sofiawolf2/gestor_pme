package br.com.taurustech.gestor.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Util {

    private Util() {
    }

    private static final String JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf";

    public static Claims getClaims(String token) {
        byte[] signingKey = Util.JWT_SECRET.getBytes();

        token = token.replace("Bearer ", "");

        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token).getBody();
    }

    public static String getLogin(String token) {
        Claims claims = getClaims(token);
        if (!isNull(claims)) {
            return claims.getSubject();
        }
        return null;
    }

    public static List<GrantedAuthority> getRoles(String token) {
        Claims claims = getClaims(token);
        if (claims == null) {
            return List.of();
        }
        var retorno = ((List<?>) claims
                .get("rol")).stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .toList();
        return converterParaGrantedAuthority(retorno);

    }

    private static List<GrantedAuthority> converterParaGrantedAuthority(List<SimpleGrantedAuthority> simplesAuthorities) {
        return simplesAuthorities.stream()
                .map(GrantedAuthority.class::cast)
                .toList();
    }


    public static boolean isTokenValid(String token) {
        Claims claims = getClaims(token);
        if (nonNull(claims)) {
            String login = claims.getSubject();
            Date expiration = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            return login != null && expiration != null && now.before(expiration);

        }
        return false;
    }

    public static String createToken(UserDetails user) {
        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        byte[] signingKey = Util.JWT_SECRET.getBytes();
        int days = 10;
        long time = (long) days * 24 /*horas*/ * 60 /*min*/ * 60 /*seg*/ * 1000  /*milis*/;
        Date expiration = new Date(System.currentTimeMillis() + time);


        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setSubject(user.getUsername())
                .setExpiration(expiration)
                .claim("rol", roles)
                .compact();
    }

    public static String getAuthLogin() {
        UserDetails user = getUserDetails();
        if(user != null){
            return user.getUsername();
        }
        return null;
    }

    public static UserDetails getUserDetails(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() != null){
            return (UserDetails) auth.getPrincipal();
        }
        return null;
    }
}
