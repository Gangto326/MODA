package com.moda.moda_api.user.application;

import com.moda.moda_api.user.domain.User;
import com.moda.moda_api.user.domain.UserRepository;
import com.moda.moda_api.user.presentation.request.SignupRequest;
import com.moda.moda_api.user.presentation.request.LoginRequest;
import com.moda.moda_api.user.presentation.request.UpdateProfileRequest;
import com.moda.moda_api.user.application.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .userId("test-id")
                .email("test@example.com")
                .password("password123")
                .nickname("testUser")
                 .role("ACTIVE")
                .build();

        testUserResponse = UserResponse.builder()
                .userId(testUser.getUserId())
                .email(testUser.getEmail())
                .nickname(testUser.getNickname())
                .role(testUser.getRole())
                .build();
    }

    @Test
    void signup_Success() {
        // Given
        SignupRequest request = new SignupRequest();
        given(userRepository.existsByEmail(any())).willReturn(false);
        given(userMapper.toUser(any())).willReturn(testUser);
        given(userRepository.save(any())).willReturn(testUser);
        given(userMapper.toUserResponse(any())).willReturn(testUserResponse);

        // When
        UserResponse response = userService.signup(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_DuplicateEmail() {
        // Given
        SignupRequest request = new SignupRequest();
        given(userRepository.existsByEmail(any())).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.signup(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");
    }

    @Test
    void login_Success() {
        // Given
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        given(userMapper.getEmailFromLoginRequest(any())).willReturn("test@example.com");
        given(userRepository.findByEmailAndRole(any(), any())).willReturn(testUser);
        given(userMapper.toUserResponse(any())).willReturn(testUserResponse);

        // When
        UserResponse response = userService.login(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void updateProfile_Success() {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest();
        given(userRepository.findById(any())).willReturn(testUser);
        given(userRepository.save(any())).willReturn(testUser);
        given(userMapper.toUserResponse(any())).willReturn(testUserResponse);

        // When
        UserResponse response = userService.updateProfile("test-id", request);

        // Then
        assertThat(response).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateProfile_UserNotFound() {
        // Given
        UpdateProfileRequest request = new UpdateProfileRequest();
        given(userRepository.findById(any())).willReturn(null);

        // When & Then
        assertThatThrownBy(() -> userService.updateProfile("test-id", request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }
}