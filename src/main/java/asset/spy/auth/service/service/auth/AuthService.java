package asset.spy.auth.service.service.auth;

import asset.spy.auth.service.dto.request.LoginRequestDto;
import asset.spy.auth.service.dto.request.TokenRefreshRequestDto;
import asset.spy.auth.service.dto.response.JwtResponseDto;

public interface AuthService {
    JwtResponseDto authenticate(LoginRequestDto loginRequestDto, String deviceType);
    JwtResponseDto refreshToken(TokenRefreshRequestDto refreshRequest, String deviceType);
}
