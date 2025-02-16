package com.moda.moda_api.user.application;

import org.springframework.stereotype.Service;

import com.moda.moda_api.common.infrastructure.TokenService;
import com.moda.moda_api.common.jwt.TokenDto;
import com.moda.moda_api.common.util.JwtUtil;
import com.moda.moda_api.user.application.response.AuthResponse;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserId;
import com.moda.moda_api.user.domain.UserRepository;
import com.moda.moda_api.user.infrastructure.PasswordEncrypt;
import com.moda.moda_api.user.presentation.request.LoginRequest;
import com.moda.moda_api.user.presentation.request.SignupRequest;

import lombok.RequiredArgsConstructor;

/**
 * User 도메인과 관련된 핵심 비즈니스 로직을 수행하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncrypt passwordEncrypt;
    private final TokenService tokenService;


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
        userRepository.save(userMapper.signupRequestToUser(request));
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
    public void logout(String accessToken) {
        UserId userId = new UserId(jwtUtil.getUserId(accessToken, "AccessToken"));

        tokenService.invalidateAccessToken(userId);
        tokenService.invalidateRefreshToken(
                userId,
                accessToken
        );
    }

    /**
     * RefreshToken을 받고, AccessToken을 재생성합니다.
     * @param refreshToken
     * @return
     */
    public TokenDto reGenerateToken(String refreshToken) {
        UserId userId = new UserId(jwtUtil.getUserId(refreshToken, "RefreshToken"));

        tokenService.validateRefreshToken(refreshToken);

        User user = userRepository.findById(userId.getValue())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 새로운 Access Token 생성
        TokenDto newAccessToken = jwtUtil.generateToken(user, "AccessToken");

        // Redis에 새로운 Access Token 저장
        tokenService.saveAccessToken(userId, newAccessToken.getTokenValue());

        return newAccessToken;
    }

    public boolean isUserNameAvailable(String userName) {
        return !userRepository.existsByUserName(userName);
    }


    /**
     * 사용자 프로필을 수정합니다.
     *
     * @param userId 수정할 사용자 ID
     * @param request 프로필 수정 요청 정보
     * @return 수정된 사용자의 정보
     * @throws IllegalArgumentException 사용자가 존재하지 않거나 닉네임이 중복되는 경우
     */
    // public UserResponse updateProfile(String userId, UpdateProfileRequest request) {
    //     User user = userRepository.findById(userId);
    //     if (user == null) {
    //         throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
    //     }
    //
    //     // 닉네임 변경 시 중복 체크
    //     if (request.getNickname() != null &&
    //             !user.getNickname().equals(request.getNickname()) &&
    //             userRepository.existsByNickname(request.getNickname())) {
    //         throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
    //     }
    //
    //     userMapper.updateUser(user, request);
    //     User updatedUser = userRepository.save(user);
    //     return userMapper.toUserResponse(updatedUser);
    // }

    /**
     * 회원 탈퇴를 처리합니다.
     *
     * @param userId 탈퇴할 사용자 ID
     * @param verificationCode 인증 코드
     * @return 탈퇴 처리 성공 여부
     */
    // UserService
    /**
     * 회원 탈퇴를 처리합니다.
     *
     * @param request 회원 탈퇴 요청 정보
     * @return 탈퇴 처리 성공 여부
     * @throws IllegalArgumentException 사용자가 존재하지 않거나 비밀번호가 일치하지 않는 경우
     */
    // public boolean deleteUser(DeleteUserRequest request) {
    //     User user = userRepository.findByEmailAndStatus(request.getEmail(), "ACTIVE")
    //         .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일이거나 비활성화된 계정입니다."));
    //
    //
    //     // 비밀번호 확인
    //     if (!user.getPassword().equals(request.getPassword())) {
    //         throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    //     }
    //
    //     // 인증 코드 확인
    //     if (!isValidVerificationCode(request.getVerificationCode())) {
    //         throw new IllegalArgumentException("인증 코드가 유효하지 않습니다.");
    //     }
    //
    //     user.delete();  // soft delete
    //     userRepository.save(user);
    //     return true;
    // }
    //
    // private boolean isValidVerificationCode(String verificationCode) {
    //     // 인증 코드 검증 로직은 나중에 구현
    //     return true;
    // }
    //
    // /**
    //  * 로그아웃을 처리합니다.
    //  * 실제 구현은 세션이나 토큰 관리 방식에 따라 달라질 수 있습니다.
    //  *
    //  * @param userId 로그아웃할 사용자 ID
    //  */
    // public void logout(String userId) {
    //     // 토큰 무효화 또는 세션 종료 로직 구현 필요
    // }
    //
    // // 인증 코드 검증 메서드
    // private void validateVerificationCode(String verificationCode) {
    //     // 인증 코드 검증 로직 구현 필요
    // }
    //
    // /**
    //  * 이메일 중복 여부를 확인합니다.
    //  *
    //  * @param email 중복 검사할 이메일
    //  * @return 중복이면 true, 아니면 false
    //  */
    // public boolean checkEmailDuplicate(String email) {
    //     return userRepository.existsByEmail(email);
    // }
    //
    // /**
    //  * 닉네임 중복 여부를 확인합니다.
    //  *
    //  * @param nickname 중복 검사할 닉네임
    //  * @return 중복이면 true, 아니면 false
    //  */
    // public boolean checkNicknameDuplicate(String nickname) {
    //     return userRepository.existsByNickname(nickname);
    // }
    //
    // /**
    //  * 사용자 프로필 정보를 조회합니다.
    //  *
    //  * @param userId 조회할 사용자 ID
    //  * @return 조회된 사용자의 프로필 정보
    //  * @throws IllegalArgumentException 사용자가 존재하지 않는 경우
    //  */
    // public UserProfileResponse getUserProfile(String userId) {
    //     User user = userRepository.findById(userId);
    //     if (user == null) {
    //         throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
    //     }
    //     return userMapper.toUserProfileResponse(user);
    // }


}