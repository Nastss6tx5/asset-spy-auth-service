package asset.spy.auth.service.service.user;

import asset.spy.auth.service.dto.request.RegisterRequestDto;
import asset.spy.auth.service.dto.response.UserInfoResponseDto;

public interface UserService {
    void register(RegisterRequestDto registerRequestDto);
    UserInfoResponseDto getUserInfo(String login);
}
