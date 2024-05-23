package net.wallet.main.wallet.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.wallet.main.wallet.users.Users;
import net.wallet.main.wallet.utility.exception.CustomeException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String SECRET_KEY = "413F4428472B4B6250645367566B5970337336763979244226452948404D6351";

    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public String extractUserId(String jwtToken) {
        return extractClaim(jwtToken, Claims::getId);
    }

    private Date extractExpirationDate(String jwtToken) {

        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public String generateToken(
            Users userDetails) {
        return generateToken(new HashMap<>(), userDetails);

    }

    public String generateToken(
            Map<String, Object> extraClaims,
            Users userDetails) {

        // SecretKey originalKey =Jwts.KeyGenerator()
        Key key = Keys.hmacShaKeyFor(getSignedKey());
        extraClaims.put("firstname", userDetails.getFirstname());
        extraClaims.put("lastname", userDetails.getLastname());
        extraClaims.put("mobile", userDetails.getMobile());
        extraClaims.put("permissionLevel", userDetails.getUsertype());
        extraClaims.put("userId", userDetails.getId());
        extraClaims.put("webAccess", 1);
        extraClaims.put("email", userDetails.getEmail());
        extraClaims.put("image", userDetails.getId());// can be different

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+10000*60*24))
                .signWith(key)
                .compact();

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        try {
            final Claims claims = extractAllClaims(token);

            return claimsResolver.apply(claims);
        } catch (io.jsonwebtoken.ExpiredJwtException w) {

            throw new CustomeException("SESSION_EXPIRED", null);
        } catch (Exception ex) {

            throw new CustomeException(ex.toString(), null);
        }

    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        return extractUsername(token).equals(userDetails.getUsername());
    }

    public boolean isTokenExpiter(String token) {

        try {
            return extractExpirationDate(token).before(new Date());
        } catch (Exception e) {

            return true;
        }
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private byte[] getSignedKey() {
        byte[] keys = Decoders.BASE64.decode(SECRET_KEY);
        return keys;

    }

}
