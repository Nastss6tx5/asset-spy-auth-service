package asset.spy.auth.service.repository;

import asset.spy.auth.service.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByExpireTimeBefore(OffsetDateTime expireTime);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByAccountIdAndDeviceType(long accountId, String deviceType);
}
