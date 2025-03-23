package asset.spy.auth.service.service.token;

import asset.spy.auth.service.model.RefreshToken;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface TokenService {
    void saveRefreshToken(String refreshToken, long accountId, String deviceType);
    void cleanupExpiredTokens();
    Optional<RefreshToken> findValidTokenByAccountIdAndDeviceType(long accountId, String deviceType,
                                                                  OffsetDateTime currentTime);
    void deleteRefreshToken(String refreshToken);
}
