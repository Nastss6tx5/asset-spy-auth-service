package asset.spy.auth.service.service.auth;

import asset.spy.auth.service.details.CustomUserDetails;
import asset.spy.auth.service.exception.AuthenticationException;
import asset.spy.auth.service.exception.InvalidRefreshTokenException;
import asset.spy.auth.service.model.Account;
import asset.spy.auth.service.model.RefreshToken;
import asset.spy.auth.service.payload.request.LoginRequest;
import asset.spy.auth.service.payload.request.TokenRefreshRequest;
import asset.spy.auth.service.payload.response.JwtResponse;
import asset.spy.auth.service.repository.AccountRepository;
import asset.spy.auth.service.repository.RefreshTokenRepository;
import asset.spy.auth.service.security.JwtTokenProvider;
import asset.spy.auth.service.service.token.TokenService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final AccountRepository accountRepository;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public JwtResponse authenticate(LoginRequest loginRequest, String deviceType) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
        );
        Account account = accountRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new AuthenticationException("Account not found"));

        OffsetDateTime currentTime = OffsetDateTime.now();
        Optional<RefreshToken> existingToken = tokenService.findValidTokenByAccountIdAndDeviceType(account.getId(),
                deviceType, currentTime);

        String jwt;
        String refreshToken;

        if (existingToken.isPresent()) {
            jwt = jwtTokenProvider.generateToken(authentication);
            refreshToken = existingToken.get().getRefreshToken();
        } else {
            jwt = jwtTokenProvider.generateToken(authentication);
            refreshToken = jwtTokenProvider.generateRefreshToken(authentication, deviceType);
            tokenService.saveRefreshToken(refreshToken, account.getId(), deviceType);
        }
        return new JwtResponse(jwt, refreshToken);

    }

    @Override
    @Transactional
    public JwtResponse refreshToken(TokenRefreshRequest request, String deviceType) {
        String requestRefreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(requestRefreshToken)) {
            throw new ValidationException("Refresh token is not valid");
        }

        String login = jwtTokenProvider.extractLogin(requestRefreshToken);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(login);

        RefreshToken existingToken = refreshTokenRepository.findByRefreshToken(requestRefreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found"));

        if (!existingToken.getDeviceType().equals(deviceType)) {
            throw new InvalidRefreshTokenException("Device type mismatch");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());

        String newJwt = jwtTokenProvider.generateToken(authentication);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication, deviceType);

        tokenService.deleteRefreshToken(requestRefreshToken);
        tokenService.saveRefreshToken(newRefreshToken, userDetails.getId(), deviceType);
        return new JwtResponse(newJwt, newRefreshToken);
    }
}
