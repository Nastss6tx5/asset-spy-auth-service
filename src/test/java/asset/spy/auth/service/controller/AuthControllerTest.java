package asset.spy.auth.service.controller;

import asset.spy.auth.service.config.IntegrationBaseTest;
import asset.spy.auth.service.constant.TestConstants;
import asset.spy.auth.service.dto.request.LoginRequestDto;
import asset.spy.auth.service.dto.request.RegisterRequestDto;
import asset.spy.auth.service.dto.request.TokenRefreshRequestDto;
import asset.spy.auth.service.dto.response.JwtResponseDto;
import asset.spy.auth.service.security.JwtTokenProvider;
import asset.spy.auth.service.util.TestDtoFactory;
import asset.spy.auth.service.util.TestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest extends IntegrationBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private TestHelper testHelper;

    @BeforeEach
    void setUp() {
        this.testHelper = new TestHelper(mockMvc, objectMapper);
    }

    @Test
    @SneakyThrows
    void testRegisterUserSuccess() {
        RegisterRequestDto request = TestDtoFactory.createRegisterRequestDto();
        testHelper.registerUserWithValidData(request);
    }

    @Test
    @SneakyThrows
    void testRegisterUserWithExistingLogin() {
        RegisterRequestDto request = TestDtoFactory.createRegisterRequestDto();
        testHelper.registerUserWithValidData(request);

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void testRegisterUserWithInvalidData() {
        RegisterRequestDto invalidRequest = TestDtoFactory.createInvalidRegisterRequestDto();

        String[] expectedMessages = {
                TestConstants.LOGIN_VALIDATION,
                TestConstants.USERNAME_VALIDATION,
                TestConstants.PASSWORD_VALIDATION,
                TestConstants.DESCRIPTION_VALIDATION,
                TestConstants.DATE_OF_BIRTH_VALIDATION,
                TestConstants.MUST_NOT_BE_BLANK
        };
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    String body = result.getResponse().getContentAsString();
                    for (String expectedMessage : expectedMessages) {
                        assertThat(body).contains(expectedMessage);
                    }
                });
    }

    @Test
    @SneakyThrows
    void testLoginSuccess() {
        testHelper.registerAndAuthenticateUserWithValidData(TestConstants.TEST_USER_AGENT);
    }

    @Test
    @SneakyThrows
    void testLoginWithInvalidCredential() {
        LoginRequestDto loginRequest = TestDtoFactory.createInvalidLoginRequestDto();

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("User-Agent", TestConstants.TEST_USER_AGENT)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void testLoginWithInvalidData() {
        LoginRequestDto invalidRequest = TestDtoFactory.createInvalidLoginRequestDto();

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("User-Agent", TestConstants.TEST_USER_AGENT)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void testRefreshTokenSuccess() {
        JwtResponseDto jwtResponse = testHelper.registerAndAuthenticateUserWithValidData(TestConstants.TEST_USER_AGENT);

        TokenRefreshRequestDto refreshRequest = TestDtoFactory.createTokenRefreshRequestDto(
                jwtResponse.getRefreshToken()
        );
        MvcResult result = mockMvc.perform(post("/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("User-Agent", TestConstants.TEST_USER_AGENT)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andReturn();

        JwtResponseDto jwtResponseDto = objectMapper.readValue(result.getResponse().getContentAsString(), JwtResponseDto.class);

        assertThat(jwtResponseDto.getAccessToken()).isNotEmpty();
        assertThat(jwtResponseDto.getRefreshToken()).isNotEmpty();
    }

    @Test
    @SneakyThrows
    void testGetUserInfoSuccess() {
        JwtResponseDto jwtResponseDto = testHelper.registerAndAuthenticateUserWithValidData(TestConstants.TEST_USER_AGENT);
        String token = jwtResponseDto.getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/auth/user/info")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(TestConstants.TEST_USERNAME))
                .andExpect(jsonPath("$.login").value(TestConstants.TEST_LOGIN))
                .andExpect(jsonPath("$.description").value(TestConstants.TEST_DESCRIPTION))
                .andExpect(jsonPath("$.dateOfBirth").value(TestConstants.TEST_DATE_OF_BIRTH.toString()))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    @SneakyThrows
    void testHetUserInfoWithInsufficientPermission() {
        String nonAuthorizedLogin = "userGuest";
        String role = "ROLE_GUEST";
        UUID externalId = UUID.randomUUID();
        String token = jwtTokenProvider.generateToken(nonAuthorizedLogin, role, externalId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/auth/user/info")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}

