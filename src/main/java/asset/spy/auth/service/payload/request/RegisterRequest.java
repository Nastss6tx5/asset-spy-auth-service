package asset.spy.auth.service.payload.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RegisterRequest {
    private String login;
    private String username;
    private String password;
    private String description;
    private LocalDate dateOfBirth;
}
