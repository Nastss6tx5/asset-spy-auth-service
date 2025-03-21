package asset.spy.auth.service.controller;

import asset.spy.auth.service.payload.request.LoginRequest;
import asset.spy.auth.service.payload.request.RegisterRequest;
import asset.spy.auth.service.payload.request.TokenRefreshRequest;
import asset.spy.auth.service.payload.response.JwtResponse;
import asset.spy.auth.service.payload.response.UserInfoResponse;
import asset.spy.auth.service.service.auth.AuthService;
import asset.spy.auth.service.service.user.UserService;
import asset.spy.auth.service.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest,
                                             @RequestHeader("User-Agent") String userAgent) {
        return ResponseEntity.ok(authService.authenticate(loginRequest, UserAgentUtil.determineUserAgent(userAgent)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody TokenRefreshRequest refreshRequest,
                                               @RequestHeader("User-Agent") String userAgent) {
        return ResponseEntity.ok(authService.refreshToken(refreshRequest, UserAgentUtil.determineUserAgent(userAgent)));
    }

    @GetMapping("/userInfo")
    public ResponseEntity<UserInfoResponse> getUserInfo(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserInfo(authentication));
    }
}
