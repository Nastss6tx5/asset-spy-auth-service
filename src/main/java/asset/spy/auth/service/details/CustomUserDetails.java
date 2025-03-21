package asset.spy.auth.service.details;

import asset.spy.auth.service.model.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final UUID externalId;
    private final String login;
    private final String username;
    private final String description;
    private final LocalDate dateOfBirth;

    @JsonIgnore
    private final String password;

    public CustomUserDetails(Account account) {
        this.id = account.getId();
        this.externalId = account.getExternalId();
        this.login = account.getLogin();
        this.username = account.getUser().getUsername();
        this.password = account.getPassword();
        this.description = account.getUser().getDescription();
        this.dateOfBirth = account.getUser().getDateOfBirth();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
