package com.moda.moda_api.user.application;

import com.moda.moda_api.user.application.response.UserProfileResponse;
import com.moda.moda_api.user.application.response.UserResponse;
import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserId;
import com.moda.moda_api.user.infrastructure.PasswordEncrypt;
import com.moda.moda_api.user.presentation.request.LoginRequest;
import com.moda.moda_api.user.presentation.request.SignupRequest;
import com.moda.moda_api.user.presentation.request.UpdateProfileRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * User 도메인 객체와 DTO 간의 변환을 담당하는 매퍼 클래스입니다.
 * 도메인 객체와 DTO 간의 변환 책임을 중앙화하여 관리합니다.
 */
@Component
public class UserMapper {


    /**
     * SignupRequest DTO를 User 도메인 객체로 변환합니다.
     *
     * @param request 회원가입 요청 DTO
     * @return 변환된 User 도메인 객체
     */
    public User signupRequestToUser(SignupRequest request) {
        return User.builder()
                .userId(new UserId(UUID.randomUUID().toString()))
                .email(request.getEmail())
                .userName(request.getUserName())
                .nickname(request.getNickname())
                .hashedPassword(PasswordEncrypt.encrypt(request.getPassword()))
                .build();
    }

    /**
     * UpdateUserRequest DTO의 정보로 User 도메인 객체를 업데이트합니다.
     *
     * @param user 업데이트할 User 도메인 객체
     * @param request 업데이트 요청 정보를 담은 DTO
     */
    // public void updateUser(User user, UpdateProfileRequest request) {
    //     // null이 아닌 필드만 업데이트
    //     if (request.getNickname() != null) {
    //         user.updateProfile(request.getNickname(), request.getProfileImage());
    //     }
    //     if (request.getPassword() != null) {
    //         user.updatePassword(request.getPassword());
    //     }
    // }


    /**
     * LoginRequest DTO의 정보를 검증을 위한 형태로 변환합니다.
     *
     * @param request 로그인 요청 DTO
     * @return 이메일 정보
     */
    // public String getEmailFromLoginRequest(LoginRequest request) {
    //     return request.getEmail();
    // }


}
