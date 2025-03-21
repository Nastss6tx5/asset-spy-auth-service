package asset.spy.auth.service.payload.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
public class UserInfoResponse {
    private String username;
    private String description;
    private LocalDate dateOfBirth;
    private OffsetDateTime createdAt;
}
