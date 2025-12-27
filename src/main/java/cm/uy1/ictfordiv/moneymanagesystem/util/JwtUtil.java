package cm.uy1.ictfordiv.moneymanagesystem.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    // 1. Génère un token avec claims (informations supplémentaires)
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), jwtExpiration);
    }

    // 2. Génère un token avec des claims spécifiques
    public String generateToken(Map<String, Object> claims, String username) {
        return createToken(claims, username, jwtExpiration);
    }

    // 3. Génère un refresh token (durée de vie plus longue)
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, refreshExpiration);
    }

    // 4. Crée un token JWT
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 5. Extrait le username du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 6. Extrait la date d'expiration du token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 7. Extrait une claim spécifique
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 8. Extrait toutes les claims du token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 9. Vérifie si le token a expiré
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 10. Valide un token (vérifie username et expiration)
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 11. Valide juste la structure et l'expiration du token
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (SignatureException e) {
            // Signature invalide
        } catch (MalformedJwtException e) {
            // Token mal formé
        } catch (ExpiredJwtException e) {
            // Token expiré
        } catch (UnsupportedJwtException e) {
            // Token non supporté
        } catch (IllegalArgumentException e) {
            // Claims vides
        }
        return false;
    }

    // 12. Génère la clé de signature à partir du secret
    private SecretKey getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 13. Vérifie si le token est un refresh token
    public Boolean isRefreshToken(String token) {
        try {
            Date expiration = extractExpiration(token);
            Date now = new Date();
            long diff = expiration.getTime() - now.getTime();
            // Un refresh token a une durée de vie > jwtExpiration
            return diff > jwtExpiration;
        } catch (Exception e) {
            return false;
        }
    }

    // 14. Renouvelle un token expiré avec un refresh token valide
    public String refreshToken(String refreshToken, UserDetails userDetails) {
        if (validateToken(refreshToken) && isRefreshToken(refreshToken)) {
            return generateToken(userDetails);
        }
        throw new RuntimeException("Refresh token invalide ou expiré");
    }
}