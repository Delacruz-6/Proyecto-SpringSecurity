package realEstate.salesianos.triana.dam.realEstate.Security.jwt;

import lombok.extern.java.Log;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Log
@Service
public class JwtTokenProvider {

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.secret:luismielmaquinaskjdfhdjfhjdghjgfjgnfgfngfgn}")
    private String jwtSecret;

    @Value ("${jwt.duration:3600}") // 1 hora
    private int jwtLifeInSeconds;

    /*
    --- NO FUNCIONA EL parserBuilder() ---
    private JwtParser parser;
    @PostConstruct
    public void init() {
        parser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build();
    }
    */


    public String generateToken(Authentication authentication) {

        Usuario user = (Usuario) authentication.getPrincipal();

        Date tokenExpirationDate = Date
                .from(LocalDateTime
                        .now()
                        .plusSeconds(jwtLifeInSeconds)
                        .atZone(ZoneId.systemDefault()).toInstant());


        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(tokenExpirationDate)
                .claim("username", user.getUsername())
                .claim("rol", user.getRol().name())
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
        //return Long.valueOf(parser.parseClaimsJws(token).getBody().getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token);
            //parser.parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            log.info("Error con el token: " + ex.getMessage());
        }
        return false;
    }
}
