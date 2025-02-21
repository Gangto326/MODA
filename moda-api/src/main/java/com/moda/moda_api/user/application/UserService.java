package com.moda.moda_api.user.application;

import org.springframework.stereotype.Service;

import com.moda.moda_api.card.exception.UnauthorizedException;
import com.moda.moda_api.category.application.service.CategoryService;
import com.moda.moda_api.common.infrastructure.TokenService;
import com.moda.moda_api.common.jwt.TokenDto;
import com.moda.moda_api.common.util.JwtUtil;
import com.moda.moda_api.user.application.response.AuthResponse;
import com.moda.moda_api.user.domain.RefreshToken;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserId;
import com.moda.moda_api.user.domain.UserRepository;
import com.moda.moda_api.user.exception.InvalidRequestException;
import com.moda.moda_api.user.exception.UserNotFoundException;
import com.moda.moda_api.user.infrastructure.PasswordEncrypt;
import com.moda.moda_api.user.presentation.request.LoginRequest;
import com.moda.moda_api.user.presentation.request.PasswordResetRequest;
import com.moda.moda_api.user.presentation.request.SignupRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * User 도메인과 관련된 핵심 비즈니스 로직을 수행하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncrypt passwordEncrypt;
    private final TokenService tokenService;
    private final CategoryService categoryService;


    /**
     * 회원가입을 처리합니다.
     *
     * @param request 회원가입 요청 정보
     * @return 생성된 사용자의 정보
     * @throws IllegalArgumentException 이메일이 이미 존재하는 경우
     */
    public boolean signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = userRepository.save(userMapper.signupRequestToUser(request));

        categoryService.initCategoryPosition(user.getUserId().getValue());

        return true;
    }

    /**
     * 로그인을 처리합니다.
     *
     * @param request 로그인 요청 정보
     * @return 로그인된 사용자의 정보
     * @throws IllegalArgumentException 이메일이나 비밀번호가 일치하지 않는 경우
     */
     public AuthResponse login(LoginRequest request) {
         // 이메일로 있는 User인지 체크
         User user = userRepository.findByUserName(request.getUserId())
             .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

         // 사용자 상태 체크
         if (user.isDeleted()) {
             throw new IllegalArgumentException("탈퇴한 계정입니다.");
         }

         // 비밀번호 체크
         if (!passwordEncrypt.verifyPassword(request.getPassword(), user.getHashedPassword())) {
             throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
         }

         TokenDto accessToken = jwtUtil.generateToken(user, "AccessToken");
         TokenDto refreshToken = jwtUtil.generateToken(user, "RefreshToken");

         // 토큰 저장
         tokenService.saveAccessToken(user.getUserId(), accessToken.getTokenValue());
         tokenService.saveRefreshToken(user.getUserId(), refreshToken);


         return AuthResponse.builder()
                 .accessToken(accessToken.getTokenValue())
                 .refreshToken(refreshToken.getTokenValue())
                 .maxAge(refreshToken.getExpiresAt())
                 .build();
     }

    /**
     * 로그아웃 합니다.
     *
     * Redis의 엑세스 토큰를 삭제하고, RefreshToken을 비활성화 합니다.
     * @param accessToken
     */
    public void logout(String refreshToken, String accessToken) {
        UserId userId = new UserId(jwtUtil.getUserId(accessToken, "AccessToken"));

        //AccessToken 비활
        tokenService.invalidateAccessToken(accessToken);

        //RefreshToken
        tokenService.invalidateRefreshToken(refreshToken);
    }

    /**
     * RefreshToken을 받고, AccessToken을 재생성합니다.
     * @param refreshToken
     * @return
     */
    public TokenDto reGenerateToken(String refreshToken) {
        // 1. refreshToken JWT 유효성 검증
        if (!jwtUtil.isValidToken(refreshToken, "RefreshToken")) {
            throw new UnauthorizedException("Refresh Token이 만료되었습니다.", "REFRESH_TOKEN_EXPIRED");
        }

        // 2. DB에서 refreshToken을 확인하고 isDead인지 확인해 ~
        RefreshToken validatedToken = tokenService.validateRefreshToken(refreshToken);

        // 3. 다 통과하면 통과해
        UserId userId = new UserId(jwtUtil.getUserId(refreshToken, "RefreshToken"));

        // 4. 해당 유저가 있는지도 검사해
        User user = userRepository.findById(userId.getValue())
            .orElseThrow(() -> new UnauthorizedException("존재하지 않는 사용자입니다.", "USER_NOT_FOUND"));

        if (user.getDeletedAt() != null) {
            throw new UnauthorizedException("탈퇴한 사용자입니다.", "USER_DELETED");
        }

        // 새로운 Access Token 생성
        TokenDto newAccessToken = jwtUtil.generateToken(user, "AccessToken");

        // Redis에 새로운 Access Token 저장
        tokenService.saveAccessToken(userId, newAccessToken.getTokenValue());

        return newAccessToken;
    }

    public boolean isUserNameAvailable(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public User findByUserName(String email)
    {
        return userRepository.findByEmail(email).get();
    }

    @Transactional
    public boolean resetPassword(PasswordResetRequest passwordResetRequest) {
        // 사용자 ID와 이메일로 사용자 찾기
        User user = userRepository.findByEmail(passwordResetRequest.getEmail())
            .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 이메일 일치 여부 확인
        if (!user.getEmail().equals(passwordResetRequest.getEmail())) {
            log.warn("비밀번호 재설정 시도: 이메일 불일치 - userId: {}", passwordResetRequest.getUserId());
            throw new InvalidRequestException("이메일이 일치하지 않습니다.");
        }

        // 비밀번호 암호화 및 업데이트
        String encodedPassword = PasswordEncrypt.encrypt(passwordResetRequest.getNewPassword());
        user.updatePassword(encodedPassword);

        // 사용자 정보 저장
        userRepository.save(user);

        return true;
    }
}