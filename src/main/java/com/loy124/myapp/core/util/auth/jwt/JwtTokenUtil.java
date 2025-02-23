package com.loy124.myapp.core.util.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import com.loy124.myapp.member.entity.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;


import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Slf4j
public class JwtTokenUtil {

    public static final String TOKEN_PREFIX = "Bearer "; // 스페이스 필요함
    public static final String HEADER = "Authorization";
    private static Long ACCESS_EXPIRE_TIME_MS = 1000 * 60 * 60l * 2; // 2시간
    private static Long REFRESH_EXPIRE_TIME_MS = 1000 * 60 * 60l * 24 * 14; //2주 24시간 * 14


    public static String getEmail(String token, String secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody().get("email", String.class);
    }

    public static Claims getBody(String token, String secretKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public static JwtStatus verify(String token, String secretKey) {
        try {
            if (token == null) {
                return JwtStatus.FALSE;
            }

            Claims body = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token).getBody();

            return JwtStatus.TRUE;


        } catch (MalformedJwtException e) {
            log.error("토큰에 구조적인 문제가 있습니다");
            return JwtStatus.FALSE;
        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료되어 유효하지 않습니다.");
            return JwtStatus.EXPIRED;
        } catch (SignatureException e) {
            log.error("토큰 서명 검증에 실패하였습니다");
            return JwtStatus.FALSE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return JwtStatus.FALSE;
        }

    }


    public static boolean isWithinOneWeek(String token, String secretKey) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build();
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        Date expiration = claimsJws.getBody().getExpiration();
        Instant now = Instant.now();
        Instant expirationInstant = expiration.toInstant();
        Instant oneWeekFromNow = now.plusSeconds(7 * 24 * 60 * 60);

        return expirationInstant.isBefore(oneWeekFromNow);
    }

    public static String createAccessToken(Member member, String key) {
        Claims claims = Jwts.claims();
        claims.put("id", member.getId());
        claims.put("email", member.getEmail());
        claims.put("role", member.getRole());

        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_TIME_MS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return TOKEN_PREFIX + token;


    }

    public static String createRefreshToken(Member member, String key) {
        Claims claims = Jwts.claims();
        claims.put("id", member.getId());
        claims.put("email", member.getEmail());
        claims.put("role", member.getRole());
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_TIME_MS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

    }

    public static String setAccessToken(Member member, HttpServletResponse response, String key) {
        String accessToken = JwtTokenUtil.createAccessToken(member, key);
//        response.setHeader(JwtTokenUtil.HEADER, accessToken);
        if (response.containsHeader(JwtTokenUtil.HEADER)) {
            response.setHeader(JwtTokenUtil.HEADER, accessToken);
        } else {
            response.addHeader(JwtTokenUtil.HEADER, accessToken);
        }

        return accessToken;
    }


    public static String setRefreshToken(Member member, HttpServletResponse response, String key) {
        String refreshToken = JwtTokenUtil.createRefreshToken(member, key);

        Cookie cookie = new Cookie("min-token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(REFRESH_EXPIRE_TIME_MS.intValue());
        response.addCookie(cookie);
        return refreshToken;
    }


    public static String setRefreshTokenSecure(Member member, HttpServletResponse response, String key) {
        String refreshToken = JwtTokenUtil.createRefreshToken(member, key);

//        Cookie cookie = new Cookie("min-token", refreshToken);
//        cookie.setSecure(true);
        ResponseCookie responseCookie = ResponseCookie.from("min-token", refreshToken)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(REFRESH_EXPIRE_TIME_MS.intValue())
                .build();

        response.addHeader("Set-Cookie", responseCookie.toString());

//        cookie.setPath("/");
//        cookie.setMaxAge(REFRESH_EXPIRE_TIME_MS.intValue());
//        response.addCookie(cookie);
        return refreshToken;
    }

    public static void logout(HttpServletResponse response, String cookieName) {


        Cookie cookie = new Cookie("min-token", null);
        cookie.setPath("/");
        cookie.setValue("");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.setHeader(HttpHeaders.AUTHORIZATION, "");
    }


}

