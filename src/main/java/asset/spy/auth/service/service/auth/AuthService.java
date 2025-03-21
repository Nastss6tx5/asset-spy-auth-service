package asset.spy.auth.service.service.auth;

import asset.spy.auth.service.payload.request.LoginRequest;
import asset.spy.auth.service.payload.request.TokenRefreshRequest;
import asset.spy.auth.service.payload.response.JwtResponse;

public interface AuthService {
    JwtResponse authenticate(LoginRequest loginRequest, String deviceType);

    JwtResponse refreshToken(TokenRefreshRequest refreshRequest, String deviceType);
}
