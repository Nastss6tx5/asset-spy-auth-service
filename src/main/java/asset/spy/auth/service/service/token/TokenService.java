package asset.spy.auth.service.service.token;

public interface TokenService {
    void saveRefreshToken(String refreshToken, long accountId, String deviceType);
    void cleanupExpiredTokens();
}
