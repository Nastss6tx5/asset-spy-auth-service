package asset.spy.auth.service.service.token;

import asset.spy.auth.service.model.RefreshToken;
import asset.spy.auth.service.repository.AccountRepository;
import asset.spy.auth.service.repository.RefreshTokenRepository;
import asset.spy.auth.service.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void saveRefreshToken(String refreshToken, long accountId, String deviceType) {
        RefreshToken token = new RefreshToken();
        token.setRefreshToken(refreshToken);
        token.setExpireTime(jwtTokenProvider.extractClaims(refreshToken).getExpiration().toInstant().atOffset(ZoneOffset.UTC));
        token.setDeviceType(deviceType);
        token.setAccount(accountRepository.findById(accountId).orElse(null));
        refreshTokenRepository.save(token);
    }

    @Override
    @Transactional
    public void cleanupExpiredTokens() {
        OffsetDateTime currentTime = OffsetDateTime.now();
        refreshTokenRepository.deleteByExpireTimeBefore(currentTime);
    }

    @Override
    @Transactional
    public Optional<RefreshToken> findValidTokenByAccountIdAndDeviceType(long accountId, String deviceType, OffsetDateTime currentTime) {
        return refreshTokenRepository.findByAccountIdAndDeviceTypeAndExpireTimeAfter(accountId, deviceType, currentTime);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
