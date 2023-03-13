package saessak.log.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import saessak.log.user.service.UserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.List;

import static io.jsonwebtoken.Header.JWT_TYPE;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    
    private final UserService userService;
    private final String key = "asdfadgasgdsxzcnzdfhtehh34hrh324y363462344g4g434h34236343424esdfgfdgndfjdfjfgjdfgfbdcvdfgadgaergead";;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            log.info("authorization : {}", authorization);

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                log.info("authorization을 잘못 보냈습니다.");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorization.split(" ")[1];

            if (TokenProvider.isExpired(token, key)) {
                log.info("토큰 만료");
                filterChain.doFilter(request, response);
                return;
            }

            String profileId = TokenProvider.getProfileId(token, key);
            log.info("profileId = {}", profileId);

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(profileId, null, List.of(new SimpleGrantedAuthority("USER")));

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰");
        }


        filterChain.doFilter(request, response);
        
    }

}
