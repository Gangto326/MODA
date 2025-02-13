package com.moda.moda_api.user.presentation;

import com.moda.moda_api.user.application.UserService;
import com.moda.moda_api.user.application.response.UserProfileResponse;
import com.moda.moda_api.user.application.response.UserResponse;
import com.moda.moda_api.user.presentation.request.DeleteUserRequest;
import com.moda.moda_api.user.application.response.ErrorResponse;
import com.moda.moda_api.user.presentation.request.LoginRequest;
import com.moda.moda_api.user.presentation.request.SignupRequest;
import com.moda.moda_api.user.application.response.SuccessResponse;
import com.moda.moda_api.user.presentation.request.UpdateProfileRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 관련 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 요청을 처리합니다.
     *
     * @param request 회원가입에 필요한 정보를 담은 요청 객체
     * @return 생성된 사용자의 정보
     */
    // @PostMapping("/signup")
    // public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
    //     try {
    //         userService.signup(request);
    //         return ResponseEntity.status(HttpStatus.CREATED)
    //             .body(new SuccessResponse("회원가입이 완료되었습니다."));
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.badRequest()
    //             .body(new ErrorResponse(e.getMessage()));
    //     }
    // }

    /**
     * 로그인 요청을 처리합니다.
     *
     * @param request 로그인에 필요한 정보를 담은 요청 객체
     * @return 로그인된 사용자의 정보
     */
    // @PostMapping("/login")
    // public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
    //     UserResponse response = userService.login(request);
    //     return ResponseEntity.ok(response);
    // }

    /**
     * 사용자 프로필 정보를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 사용자의 프로필 정보
     */
    // @GetMapping("/{userId}")
    // public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String userId) {
    //     UserProfileResponse response = userService.getUserProfile(userId);
    //     return ResponseEntity.ok(response);
    // }

    /**
     * 사용자 프로필 정보를 수정합니다.
     *
     * @param userId 수정할 사용자의 ID
     * @param request 수정할 프로필 정보를 담은 요청 객체
     * @return 수정된 사용자의 프로필 정보
     */
    // @PutMapping("/{userId}")
    // public ResponseEntity<UserResponse> updateProfile(
    //         @PathVariable String userId,
    //         @RequestBody UpdateProfileRequest request) {
    //     UserResponse response = userService.updateProfile(userId, request);
    //     return ResponseEntity.ok(response);
    // }

    /**
     * 이메일 중복 여부를 확인합니다.
     *
     * @param email 중복 검사할 이메일
     * @return 중복 여부
     */
    // @GetMapping("/check-email")
    // public ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email) {
    //     boolean isDuplicate = userService.checkEmailDuplicate(email);
    //     return ResponseEntity.ok(isDuplicate);
    // }

    /**
     * 닉네임 중복 여부를 확인합니다.
     *
     * @param nickname 중복 검사할 닉네임
     * @return 중복 여부
     */
    // @GetMapping("/check-nickname")
    // public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestParam String nickname) {
    //     boolean isDuplicate = userService.checkNicknameDuplicate(nickname);
    //     return ResponseEntity.ok(isDuplicate);
    // }

    // @DeleteMapping
    // public ResponseEntity<Boolean> deleteUser(@RequestBody DeleteUserRequest request) {
    //     return ResponseEntity.ok(userService.deleteUser(request));
    // }
}
