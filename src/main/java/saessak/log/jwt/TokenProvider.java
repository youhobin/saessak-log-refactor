package saessak.log.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saessak.log.jwt.dto.TokenResponse;

import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

    // 만료기간은 일단 일주일.
    private final long tokenValidityInMilliseconds = 7 * 24 * 60 * 1_000;
    private final String key = "asdfadgasgdsxzcnzdfhtehh34hrh324y363462344g4g434h34236343424esdfgfdgndfjdfjfgjdfgfbdcvdfgadgaergead";

    public TokenResponse createToken(Long userId, String profileId) {

        long now = (new Date()).getTime();

        String token = Jwts.builder()
                .claim("id", userId.toString())
                .claim("profileId", profileId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + tokenValidityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        //jwtToken 생성해줌.
        return new TokenResponse(token);
    }

    //토큰을 다시 payload 로 반환.
    public static String getProfileId(String token, String key) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().get("profileId", String.class);
    }

    public static boolean isExpired(String token, String key) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }

}