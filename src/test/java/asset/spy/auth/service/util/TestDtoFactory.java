package asset.spy.auth.service.util;

import asset.spy.auth.service.constant.TestConstants;
import asset.spy.auth.service.dto.request.LoginRequestDto;
import asset.spy.auth.service.dto.request.RegisterRequestDto;
import asset.spy.auth.service.dto.request.TokenRefreshRequestDto;

import java.time.LocalDate;

public class TestDtoFactory {

    public static RegisterRequestDto createRegisterRequestDto() {
        RegisterRequestDto registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setLogin(TestConstants.TEST_LOGIN);
        registerRequestDto.setUsername(TestConstants.TEST_USERNAME);
        registerRequestDto.setPassword(TestConstants.TEST_PASSWORD);
        registerRequestDto.setDescription(TestConstants.TEST_DESCRIPTION);
        registerRequestDto.setDateOfBirth(TestConstants.TEST_DATE_OF_BIRTH);
        return registerRequestDto;
    }

    public static RegisterRequestDto createInvalidRegisterRequestDto() {
        RegisterRequestDto invalidRequest = new RegisterRequestDto();
        invalidRequest.setLogin("");
        invalidRequest.setUsername("A");
        invalidRequest.setPassword("123");
        invalidRequest.setDescription("");
        invalidRequest.setDateOfBirth(LocalDate.now().plusYears(1));
        return invalidRequest;
    }

    public static LoginRequestDto createLoginRequestDto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setLogin(TestConstants.TEST_LOGIN);
        loginRequestDto.setPassword(TestConstants.TEST_PASSWORD);
        return loginRequestDto;
    }

    public static LoginRequestDto createInvalidLoginRequestDto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setLogin("invalidLogin");
        loginRequestDto.setPassword("invalidPassword");
        return loginRequestDto;
    }

    public static TokenRefreshRequestDto createTokenRefreshRequestDto(String refreshToken) {
        TokenRefreshRequestDto tokenRefreshRequestDto = new TokenRefreshRequestDto();
        tokenRefreshRequestDto.setRefreshToken(refreshToken);
        return tokenRefreshRequestDto;
    }
}
