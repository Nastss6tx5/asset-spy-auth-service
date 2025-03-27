package asset.spy.auth.service.controller;

import asset.spy.auth.service.dto.request.LoginRequestDto;
import asset.spy.auth.service.dto.request.RegisterRequestDto;
import asset.spy.auth.service.dto.request.TokenRefreshRequestDto;
import asset.spy.auth.service.dto.response.JwtResponseDto;
import asset.spy.auth.service.dto.response.UserInfoResponseDto;
import asset.spy.auth.service.service.auth.AuthService;
import asset.spy.auth.service.service.user.UserService;
import asset.spy.auth.service.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto registerRequestDto) {
        userService.register(registerRequestDto);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                                @RequestHeader("User-Agent") String userAgent) {
        return ResponseEntity.ok(authService.authenticate(loginRequestDto, UserAgentUtil.determineUserAgent(userAgent)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refresh(@RequestBody TokenRefreshRequestDto refreshRequest,
                                                  @RequestHeader("User-Agent") String userAgent) {
        return ResponseEntity.ok(authService.refreshToken(refreshRequest, UserAgentUtil.determineUserAgent(userAgent)));
    }

    @GetMapping("/user/info")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@RequestAttribute("login") String login) {
        return ResponseEntity.ok(userService.getUserInfo(login));
    }
}
