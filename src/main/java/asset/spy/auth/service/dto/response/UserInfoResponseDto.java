package asset.spy.auth.service.dto.response;

import asset.spy.auth.service.model.Role;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class UserInfoResponseDto {
    private String username;
    private String login;
    private String description;
    private LocalDate dateOfBirth;
    private OffsetDateTime createdAt;
    private Role role;
}
