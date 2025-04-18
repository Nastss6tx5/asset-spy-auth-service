package asset.spy.auth.service.repository;

import asset.spy.auth.service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByLogin(String login);
    boolean existsByLogin(String username);
}
