package asset.spy.auth.service.util;

import asset.spy.auth.service.service.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final TokenServiceImpl tokenService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens() {
        tokenService.cleanupExpiredTokens();
    }
}
