package saessak.log.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import saessak.log.comment.security.principal.PrincipalDetailService;
import saessak.log.jwt.dto.TokenResponse;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    // 만료기간은 일단 일주일.
    private final long tokenValidityInMilliseconds = 7 * 24 * 60 * 1_000;
    private final String secret;
    private final PrincipalDetailService principalDetailService;

    private Key key;

    public TokenProvider(
        @Value("${jwt.secret}") String secret,
        PrincipalDetailService principalDetailService) {
        this.secret = secret;
        this.principalDetailService = principalDetailService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenResponse createToken(Long userId, String profileId, Authentication authenticate) {
        String authorities = authenticate.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        String token = Jwts.builder()
                .claim("id", userId.toString())
                .claim("profileId", profileId)
                .claim("role", authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + tokenValidityInMilliseconds))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        //jwtToken 생성해줌.
        return new TokenResponse(token);
    }
    
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        List<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String profileId = String.valueOf(claims.get("profileId"));

        UserDetails principal = principalDetailService.loadUserByUsername(profileId);

        return new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }

        return false;
    }

}