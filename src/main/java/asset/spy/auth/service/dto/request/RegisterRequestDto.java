package asset.spy.auth.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequestDto {

    @NotBlank
    @Size(min = 2, max = 50, message = "Login must be between 2 and 50 characters")
    private String login;

    @NotBlank
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @NotBlank
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @NotBlank
    @Size(min = 6, max = 200, message = "Description must be between 6 and 200 characters")
    private String description;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}
