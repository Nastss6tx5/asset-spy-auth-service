package asset.spy.auth.service.service.user;

import asset.spy.auth.service.payload.request.RegisterRequest;
import asset.spy.auth.service.payload.response.UserInfoResponse;
import org.springframework.security.core.Authentication;

public interface UserService {
    void register(RegisterRequest registerRequest);

    UserInfoResponse getUserInfo(Authentication authentication);
}
