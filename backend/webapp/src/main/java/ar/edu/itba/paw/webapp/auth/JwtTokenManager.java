package ar.edu.itba.paw.webapp.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class JwtTokenManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenManager.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Resource(name = "jwtSignKey")
    private byte[] signingKey;

    public String generateToken(final String username) {

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String token) {
        if (!StringUtils.isEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            try {

                Jws<Claims> parsedToken = Jwts.parser()
                        .setSigningKey(signingKey)
                        .parseClaimsJws(token.replace("Bearer ", ""));

                String username = parsedToken
                        .getBody()
                        .getSubject();

                final UserDetails user = userDetailsService.loadUserByUsername(username);
                if (!StringUtils.isEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
                }
            } catch (ExpiredJwtException exception) {
                LOGGER.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            } catch (UnsupportedJwtException exception) {
                LOGGER.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
            } catch (MalformedJwtException exception) {
                LOGGER.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            } catch (SignatureException exception) {
                LOGGER.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
            } catch (IllegalArgumentException exception) {
                LOGGER.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
            }
        }

        return null;
    }
}
