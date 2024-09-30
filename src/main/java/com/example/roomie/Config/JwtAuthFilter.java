package com.example.roomie.Config;

import com.example.roomie.Entity.User;
import com.example.roomie.JWT.JwtServiceImpl;
import com.example.roomie.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login"; // JWT filter를 적용하지 않는 URL
    private static final String PASSWORD = "RoomiePassword"; // Oauth2 로그인은 비밀번호가 없어서 임의로 설정 (사실 랜덤값으로 설정하는게 좋겠지만 일단은 이렇게 함)

    private final JwtServiceImpl jwtService; // jwt
    private final UserRepository userRepository; // 사용자 jpa

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL))
        {
            filterChain.doFilter(request, response); // login 요청이 들어오면 다음 필터 호출하게 함 -> 아래 return 줄로 이동
            return; // return을 통해 현재 필터 진행을 막기 (없으면 필터를 계속 진행함) => 즉, login 요청이 들어오면 필터를 진행하지 않고 종료하겠다는 뜻. (return으로 doFileterInternal()을 종료함)
        }

        /**
         * 사용자의 request (요청 헤더)에서 refreshToken을 추출 -> RefreshToken이 없거나 유효하지 않으면(DB와 다른 경우) null 반환
         * 사용자의 요청 헤더에 refreshToken이 있으면 accessToken이 만료되어 요청한 경우밖에 없기 때문에 위 경우를 제외하면 추출한 refreshToken은 모두 null
         */
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)  // 이중 콜론 연산자 : 자바8에서 추가된 메소드 참조 연산자(람다식) -> (token) -> jwtService.isTokenVaild(token)과 같다.
                .orElse(null);   // filter에서 token을 받아 유효성 여부를 확인한 후, 해당 반환값이 true면 이 부분은 실행하지 않지만 false면 refreshToken에 null이 저장됨

        // orElse : 자바 8에서 추가됨. Optional 클래스에서 제공하는 메서드로, 값이 존재하면 그 값을 반환하고 값이 존재하지 않으면 대체값을 반환함.
        // Optional : null을 안전하게 다루기 위해 도입된 클래스. 값이 있을 수도 있고 없을 수도 있는 상황에서 null 처리를 좀 더 직관적이고 안전하게 할 수 있게 함

        /**
         * refreshToken이 요청 헤더인 request에 존재할 경우, 사용자의 AccessToken이 만료되었음을 의미하기 때문에 refreshToken이 DB에 저장된 값과 일치하는지 확인
         * 일치하면 AccessToken을 재발급
         */
        if(refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);  // AccessToken과 RefreshToken을 재발급하는 메서드
            return; // 재발급 완료 시 이후 로직은 실행되면 안 되기 때문에 return으로 종료 -> refreshToken을 보낸 경우, accessToken을 재발급하고 인증 처리는 하지 않아야 하기 때문
        }

        /**
         * RefreshToken이 존재하지 않거나 유효하지 않을 때 처리하는 로직. 내부 메서드를 통해 AccessToken의 유효성을 검증하고 인증 성공, 실패 처리를 함
         * refreshToken이 없거나 유효하지 않다면, accessToken을 검사하고 인증을 처리하는 로직 수행
         * accessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다른 필터로 넘어가기 때문에 403 에러 발생
         * accessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
         */
        if(refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    /**
     * 리프레시 토큰으로 유저 정보를 찾고 액세스 토큰과 리프레시 토큰을 재발급하는 메서드
     * 파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고, 해당 유저가 있으면 createAccessToken()으로 accessToken 생성.
     * 그리고 reIssueRefreshToken()으로 리프레시 토큰을 재발급하고 DB에 리프레시 토큰을 업데이트.
     * 이후 sendAccessTokenAndRefreshToken()으로 응답 헤더 보내기
     * @param response
     * @param refreshToken
     */
    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssueRefreshToken = reIssueRefreshToken(user);
                    jwtService.sendAllToken(response, jwtService.createAccessToken(user.getId()), reIssueRefreshToken); // 헤더에 재발급한 accessToken과 refreshToken을 저장함
                });
    }

    /**
     * 리프레시 토큰을 재발급하고 DB에 리프레시 토큰을 업데이트하는 메서드
     * createRefreshToken()으로 리프레시 토큰을 재발급하고 DB에 재발급한 리프레시 토큰을 업데이트 후 Flush
     * @param user
     * @return
     */
    private String reIssueRefreshToken(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user); // DB에 즉시 데이터 반영 (save()는 영속성 컨텍스트에 저장해서 실제 DB 저장을 하려면 추후 flush나 commit 메서드가 실행되어야 함)
        return reIssuedRefreshToken; // 재발급한 리프레시 토큰 반환
    }

    /**
     * 액세스 토큰을 체크하고 인증 처리를 진행하는 메서드
     * request에서 extractAccessToken()으로 액세스 토큰을 추출하고 isTokenVaild()로 유효한 토큰인지 검증함
     * 유효한 토큰이면 액세스 토큰에서 extractId에서 ID를 추출하고 findById()로 해당 이메일을 사용하는 사용자 객체를 반환함
     * 그 사용자 객체를 saveAuthentication()으로 인증 처리해서 인증 허가 처리된 객체를 SecurityContextHolder에 담고 다음 인증 필터로 진행함
     * @param request
     * @param response
     * @param filterChain
     */
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");

        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractId(accessToken)
                        .ifPresent(id -> userRepository.findById(Long.valueOf(id))
                                .ifPresent(this::saveAuthentication)));
    }

    /**
     * [인증 허가 메서드]
     *
     * 파라미터의 유저 값 : 생성한 회원 객체
     * 빌더의 유저 값 : userDetails의 User 객체
     *
     * spring security를 사용해 특정 사용자를 인증하는 방법으로, 사용자를 인증하고 인증 정보를 SecurityContextHolder에 저장하는 과정임
     *
     * saveAuthentication() : User 객체를 받아 해당 사용자를 인증하는 작업을 수행함. 이 User 객체는 앱 내에서 사용자를 나타내는 클래스로, DB에서 가져온 사용자 정보임.
     *
     * UserDetails 객체 : spring security가 사용자 정보를 저장하는데 사용하는 인터페이스. 아래 코드에서 해당 객체에 username과 password를 넣고 있음
     *     username : 사용자의 고유한 ID를 사용자 이름으로 설정
     *     password : OAuth2에서는 사용자가 비밀번호를 설정하지 않음. 따라서 임의의 비밀번호를 사용해야 하기 때문에 따로 설정한 값이 들어감
     *
     * Authentication 객체 : 사용자에 대한 인증 정보를 담고 있음. 이 객체는 Spring security에서 인증된 사용자를 나타내는 데 사용함
     *
     * UsernamePasswordAuthenticationToken() : 인증이 끝나고 Context에 등록될 Authentication 객체를 생성함
     *
     * @param user
     */
    private void saveAuthentication(User user) {
        String password = PASSWORD;

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getId().toString())
                .password(password)
                .build();

        // userDetails : 사용자의 userDetails 값이 들어감
        // null : 사용자의 자격 증명 (보통 비밀번호)가 들어가지만 인증이 이미 완료된 상태이기 때문에 null로 설정해도 무방함
        // 세 번째 인자에는 사용자의 권한이 들어감. 하지만 따로 설정하지 않았기 때문에 추가하지 않음
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContextHolder.getContext().setAuthentication(authentication); // 설정한 인증 정보를 앱 전반에서 참조할 수 있도록 설정. 사용자가 로그인 한 것처럼 동작함
    }
}
