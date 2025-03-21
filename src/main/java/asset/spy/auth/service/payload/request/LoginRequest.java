package asset.spy.auth.service.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String login;
    private String password;
}
