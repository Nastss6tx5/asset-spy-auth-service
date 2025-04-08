package asset.spy.auth.service.service.auth;

import asset.spy.auth.service.details.CustomUserDetails;
import asset.spy.auth.service.exception.InvalidRefreshTokenException;
import asset.spy.auth.service.model.Account;
import asset.spy.auth.service.model.RefreshToken;
import asset.spy.auth.service.dto.request.LoginRequestDto;
import asset.spy.auth.service.dto.request.TokenRefreshRequestDto;
import asset.spy.auth.service.dto.response.JwtResponseDto;
import asset.spy.auth.service.repository.RefreshTokenRepository;
import asset.spy.auth.service.security.JwtTokenProvider;
import asset.spy.auth.service.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public JwtResponseDto authenticate(LoginRequestDto loginRequestDto, String deviceType) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getLogin(), loginRequestDto.getPassword())
        );
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(userDetails.getLogin(), userDetails.getRole().toString(),
                userDetails.getExternalId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getLogin(), deviceType,
                userDetails.getExternalId());
        tokenService.saveRefreshToken(refreshToken, userDetails.getId(), deviceType);
        return new JwtResponseDto(jwt, refreshToken);
    }

    @Override
    @Transactional
    public JwtResponseDto refreshToken(TokenRefreshRequestDto request, String deviceType) {
        String requestRefreshToken = request.getRefreshToken();

        jwtTokenProvider.validateToken(requestRefreshToken);

        String login = jwtTokenProvider.extractLogin(requestRefreshToken);
        String role = jwtTokenProvider.extractRole(requestRefreshToken);
        UUID externalId = jwtTokenProvider.extractExternalId(requestRefreshToken);

        RefreshToken existingToken = refreshTokenRepository.findByRefreshToken(requestRefreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
        Account account = existingToken.getAccount();
        if (!existingToken.getDeviceType().equals(deviceType)) {
            throw new InvalidRefreshTokenException("Device type mismatch");
        }

        String newJwt = jwtTokenProvider.generateToken(login, role, externalId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(login, deviceType, externalId);

        tokenService.saveRefreshToken(newRefreshToken, account.getId(), deviceType);
        return new JwtResponseDto(newJwt, newRefreshToken);
    }
}
