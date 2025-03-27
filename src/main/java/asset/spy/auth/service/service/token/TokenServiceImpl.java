package asset.spy.auth.service.service.token;

import asset.spy.auth.service.model.RefreshToken;
import asset.spy.auth.service.repository.AccountRepository;
import asset.spy.auth.service.repository.RefreshTokenRepository;
import asset.spy.auth.service.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void saveRefreshToken(String refreshToken, long accountId, String deviceType) {
        OffsetDateTime expireTime = jwtTokenProvider.extractExpiration(refreshToken);

        RefreshToken token = refreshTokenRepository.findByAccountIdAndDeviceType(accountId, deviceType)
                .orElseGet(() -> {
                    RefreshToken newToken = new RefreshToken();
                    newToken.setAccount(accountRepository.findById(accountId)
                            .orElseThrow(() -> new UsernameNotFoundException("Account not found")));
                    newToken.setDeviceType(deviceType);
                    return newToken;
                });

        token.setRefreshToken(refreshToken);
        token.setExpireTime(expireTime);
        refreshTokenRepository.save(token);
    }
}
