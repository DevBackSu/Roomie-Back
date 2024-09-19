package com.example.roomie.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j  // logging
public class JwtServiceImpl {

    // 프로퍼티 주입부
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessToken;

    @Value("${jwt.refresh.expiration}")
    private Long refreshToken;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String BEARER = "Bearer";
    private static final String ACCESS_TOKEN_SUB = "AccessToken";
    private static final String REFRESH_TOKEN_SUB = "RefreshToken";
    private static final String ID_CLAIM = "user_id";

    public String toString(){
        return "secretKey : " + secretKey + "\naccessToken : " + accessToken + "\nrefreshToken : " + refreshToken + "\naccessHeader : " + accessHeader + "\nrefreshHeader : "
                + refreshHeader;
    }

    // AccessToken 생성
    public String createAccessToken(String user_id) {
        Date now = new Date();
        return JWT.create() //JWT 토큰을 생성하는 빌더
                .withSubject(ACCESS_TOKEN_SUB) // JWT의 subject 지정
                .withExpiresAt(new Date(now.getTime() + accessToken)) // 토큰 만료 시간 설정
                .withClaim(ID_CLAIM, user_id) // payload에 넣을 claim값
                .sign(Algorithm.HMAC512(secretKey));  // HMAC512 알고리즘을 사용해 지정한 secret 키로 암호화
    }

    // RefreshToken 생성
    // claim이 필요 없기 때문에 추가 X
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUB)
                .withExpiresAt(new Date(now.getTime() + refreshToken))
                .sign(Algorithm.HMAC512(secretKey));
    }

    // AccessToken을 헤더에 실어서 보내기
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    //AccessToken이랑 RefreshToken을 헤더에 실어서 보내기
    public void sendAllToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("AccessToken과 RefreshToken의 헤더 설정을 완료함");
    }

    /**
     * 헤더에서 RefreshToken 추출
     * 순수 토큰 값만 가져오기 위해 헤더를 가져온 후 Bearer를 삭제
     * 일반적으로 토큰은 헤더의 Authorization 필드에 담아 보내지는데, Authrization 필드의 형식은 <type> <credentials>임.
     * 여기서 Bearer는 type에 해당하며, JWT 혹은 OAuth에 대한 토큰을 사용함을 나타냄
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }
}
