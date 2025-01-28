package com.moda.moda_api.user.application;

import com.moda.moda_api.user.application.response.UserProfileResponse;
import com.moda.moda_api.user.application.response.UserResponse;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserRepository;
import com.moda.moda_api.user.presentation.request.DeleteUserRequest;
import com.moda.moda_api.user.presentation.request.LoginRequest;
import com.moda.moda_api.user.presentation.request.SignupRequest;
import com.moda.moda_api.user.presentation.request.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * User 도메인과 관련된 핵심 비즈니스 로직을 수행하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * 회원가입을 처리합니다.
     *
     * @param request 회원가입 요청 정보
     * @return 생성된 사용자의 정보
     * @throws IllegalArgumentException 이메일이 이미 존재하는 경우
     */
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = userMapper.toUser(request);
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    /**
     * 로그인을 처리합니다.
     *
     * @param request 로그인 요청 정보
     * @return 로그인된 사용자의 정보
     * @throws IllegalArgumentException 이메일이나 비밀번호가 일치하지 않는 경우
     */
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndStatus(
                userMapper.getEmailFromLoginRequest(request),
                "ACTIVE"
        );

        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 이메일입니다.");
        }

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return userMapper.toUserResponse(user);
    }

    /**
     * 사용자 프로필을 수정합니다.
     *
     * @param userId 수정할 사용자 ID
     * @param request 프로필 수정 요청 정보
     * @return 수정된 사용자의 정보
     * @throws IllegalArgumentException 사용자가 존재하지 않거나 닉네임이 중복되는 경우
     */
    public UserResponse updateProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        // 닉네임 변경 시 중복 체크
        if (request.getNickname() != null &&
                !user.getNickname().equals(request.getNickname()) &&
                userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        userMapper.updateUser(user, request);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }

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
    public boolean deleteUser(DeleteUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }

        // 비밀번호 확인
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 인증 코드 확인
        if (!isValidVerificationCode(request.getVerificationCode())) {
            throw new IllegalArgumentException("인증 코드가 유효하지 않습니다.");
        }

        user.delete();  // soft delete
        userRepository.save(user);
        return true;
    }

    private boolean isValidVerificationCode(String verificationCode) {
        // 인증 코드 검증 로직은 나중에 구현
        return true;
    }

    /**
     * 로그아웃을 처리합니다.
     * 실제 구현은 세션이나 토큰 관리 방식에 따라 달라질 수 있습니다.
     *
     * @param userId 로그아웃할 사용자 ID
     */
    public void logout(String userId) {
        // 토큰 무효화 또는 세션 종료 로직 구현 필요
    }

    // 인증 코드 검증 메서드
    private void validateVerificationCode(String verificationCode) {
        // 인증 코드 검증 로직 구현 필요
    }

    /**
     * 이메일 중복 여부를 확인합니다.
     *
     * @param email 중복 검사할 이메일
     * @return 중복이면 true, 아니면 false
     */
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 닉네임 중복 여부를 확인합니다.
     *
     * @param nickname 중복 검사할 닉네임
     * @return 중복이면 true, 아니면 false
     */
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 사용자 프로필 정보를 조회합니다.
     *
     * @param userId 조회할 사용자 ID
     * @return 조회된 사용자의 프로필 정보
     * @throws IllegalArgumentException 사용자가 존재하지 않는 경우
     */
    public UserProfileResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        return userMapper.toUserProfileResponse(user);
    }


}