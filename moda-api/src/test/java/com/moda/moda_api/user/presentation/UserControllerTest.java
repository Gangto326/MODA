//package com.moda.moda_api.user.presentation;
//
//import com.moda.moda_api.user.application.UserService;
//import com.moda.moda_api.user.application.response.UserResponse;
//import com.moda.moda_api.user.application.response.UserProfileResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDateTime;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    void signup_Success() throws Exception {
//        // Given
//        String requestJson = "{\"email\":\"test@example.com\",\"password\":\"password123\"}";
//
//        UserResponse response = UserResponse.builder()
//                .userId("test-id")
//                .email("test@example.com")
//                .nickname("testUser")
//                .profileImage("profile.jpg")
//                .status("ACTIVE")
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        given(userService.signup(any())).willReturn(response);
//
//        // When & Then
//        mockMvc.perform(post("/api/user/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value("test-id"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//    }
//
//    @Test
//    void login_Success() throws Exception {
//        // Given
//        String requestJson = "{\"email\":\"test@example.com\",\"password\":\"password123\"}";
//
//        UserResponse response = UserResponse.builder()
//                .userId("test-id")
//                .email("test@example.com")
//                .nickname("testUser")
//                .profileImage("profile.jpg")
//                .status("ACTIVE")
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        given(userService.login(any())).willReturn(response);
//
//        // When & Then
//        mockMvc.perform(post("/api/user/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value("test-id"));
//    }
//
//    @Test
//    void getUserProfile_Success() throws Exception {
//        // Given
//        UserProfileResponse response = UserProfileResponse.builder()
//                .userId("test-id")
//                .email("test@example.com")
//                .nickname("testUser")
//                .profileImage("profile.jpg")
//                .status("ACTIVE")
//                .createdAt(LocalDateTime.now())
//                .isDeleted(false)
//                .build();
//
//        given(userService.getUserProfile("test-id")).willReturn(response);
//
//        // When & Then
//        mockMvc.perform(get("/api/user/test-id"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value("test-id"))
//                .andExpect(jsonPath("$.email").value("test@example.com"));
//    }
//
//    @Test
//    void updateProfile_Success() throws Exception {
//        // Given
//        String requestJson = "{\"nickname\":\"newNickname\",\"profileImage\":\"new-image.jpg\"}";
//
//        UserResponse response = UserResponse.builder()
//                .userId("test-id")
//                .email("test@example.com")
//                .nickname("newNickname")
//                .profileImage("new-image.jpg")
//                .status("ACTIVE")
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        given(userService.updateProfile(any(), any())).willReturn(response);
//
//        // When & Then
//        mockMvc.perform(put("/api/user/test-id")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.nickname").value("newNickname"))
//                .andExpect(jsonPath("$.profileImage").value("new-image.jpg"));
//    }
//
//    @Test
//    void checkEmailDuplicate_Success() throws Exception {
//        // Given
//        given(userService.checkEmailDuplicate("test@example.com")).willReturn(false);
//
//        // When & Then
//        mockMvc.perform(get("/api/user/check-email")
//                        .param("email", "test@example.com"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("false"));
//    }
//
//    @Test
//    void checkNicknameDuplicate_Success() throws Exception {
//        // Given
//        given(userService.checkNicknameDuplicate("testUser")).willReturn(false);
//
//        // When & Then
//        mockMvc.perform(get("/api/user/check-nickname")
//                        .param("nickname", "testUser"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("false"));
//    }
//}
