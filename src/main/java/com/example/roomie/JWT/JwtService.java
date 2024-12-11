package com.example.roomie.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.roomie.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Getter
@Slf4j
@RequiredArgsConstructor
public class JwtService {

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

    private final UserRepository userRepository;
    private static final String BEARER = "Bearer";
    private static final String ACCESS_TOKEN_SUB = "AccessToken";
    private static final String REFRESH_TOKEN_SUB = "RefreshToken";
    private static final String ID_CLAIM = "user_id";

    public String toString() {
        return "secretKey : " + secretKey + "\naccessToken : " + accessToken + "\nrefreshToken : " + refreshToken + "\naccessHeader : " + accessHeader + "\nrefreshHeader : "
                + refreshHeader;
    }

    // AccessToken 생성
    public String createAccessToken(Long user_id) {
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
                .map(refreshToken -> refreshToken.replace(BEARER, "")); // 토큰에서 Bearer 삭제 부분 -> credentials(자격증명)만 남음
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해 헤더를 가져온 후 Berer를 삭제
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    /**
     * Bearer XXX 형식으로 반환된 access token에서 순수 토큰만 반환
     */
    public String extractTokenAccessToken(String accessToken) {
        return accessToken.replace(BEARER, "");
    }

    /**
     * AccessToken에서 id값 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AccessToken 검증
     * 유효하면 getClaim()으로 id 추출
     * 유효하지 않으면 빈 Optional 객체 반환
     */
    public Optional<String> extractId(String accessToken) {
        try {
            return  // 유효성 검사를 위한 알고리즘이 있는 JWT verifier builder 반환
                    Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                            // 반환된 builder로 JWT verifier 생성
                            .build()
                            // accessToken을 검증하고 유효하지 않으면 예외를 발생시킴
                            .verify(accessToken)
                            // Claim 가져오기 (id값)
                            .getClaim(ID_CLAIM)
                            // 가져온 claim을 String으로 변환해 return
                            .asString());
        } catch (Exception e) {
            log.error("Access Token이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    /**
     * RefreshToken DB 업데이트
     */
    public void updateRefreshToken(Long id, String refreshToken) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        // findByid를 통해 반환된 Optional 객체에 값이 존재한다면 해당 값을 가지고 수행할 로직을 넘김
                        // 만약 Optional 객체가 비어 있을 경우, 두 번째 인자의 람다 함수가 실행됨
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 사용자가 없습니다.")
                );
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
}