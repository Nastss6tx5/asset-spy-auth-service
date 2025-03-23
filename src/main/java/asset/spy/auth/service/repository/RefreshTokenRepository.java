package asset.spy.auth.service.repository;

import asset.spy.auth.service.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByAccountIdAndDeviceTypeAndExpireTimeAfter(Long accountId, String deviceType,
                                                                          OffsetDateTime currentTime);
    @Modifying
    void deleteByExpireTimeBefore(OffsetDateTime expireTimeBefore);
    void deleteByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
