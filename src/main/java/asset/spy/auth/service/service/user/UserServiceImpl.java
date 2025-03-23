package asset.spy.auth.service.service.user;

import asset.spy.auth.service.exception.RegistrationException;
import asset.spy.auth.service.model.Account;
import asset.spy.auth.service.model.User;
import asset.spy.auth.service.payload.request.RegisterRequest;
import asset.spy.auth.service.payload.response.UserInfoResponse;
import asset.spy.auth.service.repository.AccountRepository;
import asset.spy.auth.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (accountRepository.existsByLogin(registerRequest.getLogin())) {
            throw new RegistrationException("Login " + registerRequest.getLogin() + " already exists");
        }
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RegistrationException("Username " + registerRequest.getUsername() + " already exists");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .description(registerRequest.getDescription())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .build();
        userRepository.save(user);

        Account account = Account.builder()
                .login(registerRequest.getLogin())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .user(user)
                .externalId(UUID.randomUUID())
                .build();
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public UserInfoResponse getUserInfo(Authentication authentication) {
        String login = authentication.getName();
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + login + " not found"));

        return UserInfoResponse.builder()
                .username(account.getUser().getUsername())
                .description(account.getUser().getDescription())
                .dateOfBirth(account.getUser().getDateOfBirth())
                .createdAt(account.getUser().getCreatedAt())
                .build();
    }
}
