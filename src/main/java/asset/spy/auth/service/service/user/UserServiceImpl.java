package asset.spy.auth.service.service.user;

import asset.spy.auth.service.exception.RegistrationException;
import asset.spy.auth.service.mapper.RegisterMapper;
import asset.spy.auth.service.mapper.UserMapper;
import asset.spy.auth.service.model.Account;
import asset.spy.auth.service.model.User;
import asset.spy.auth.service.dto.request.RegisterRequestDto;
import asset.spy.auth.service.dto.response.UserInfoResponseDto;
import asset.spy.auth.service.repository.AccountRepository;
import asset.spy.auth.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RegisterMapper registerMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void register(RegisterRequestDto registerRequestDto) {
        if (accountRepository.existsByLogin(registerRequestDto.getLogin())) {
            throw new RegistrationException("Login " + registerRequestDto.getLogin() + " already exists");
        }
        if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
            throw new RegistrationException("Username " + registerRequestDto.getUsername() + " already exists");
        }

        User user = registerMapper.toUser(registerRequestDto);
        Account account = registerMapper.toAccount(registerRequestDto);
        account.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        account.setUser(user);
        user.setAccount(account);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserInfoResponseDto getUserInfo(String login) {
        Account account = accountRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Account " + login + " not found"));
        return userMapper.toDto(account.getUser());
    }
}
