package asset.spy.auth.service.config;

import asset.spy.auth.service.constant.TestConstants;
import asset.spy.auth.service.dto.request.LoginRequestDto;
import asset.spy.auth.service.dto.request.RegisterRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class IntegrationBaseTest {
    protected static RegisterRequestDto registerRequest;
    protected static LoginRequestDto loginRequest;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("asset_spy_test_db")
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withInitScript("create_user_table_for_test.sql");

    @BeforeEach
    void initializeDto() {
        registerRequest = new RegisterRequestDto();
        registerRequest.setLogin(TestConstants.TEST_LOGIN);
        registerRequest.setUsername(TestConstants.TEST_USERNAME);
        registerRequest.setPassword(TestConstants.TEST_PASSWORD);
        registerRequest.setDescription(TestConstants.TEST_DESCRIPTION);
        registerRequest.setDateOfBirth(TestConstants.TEST_DATE_OF_BIRTH);

        loginRequest = new LoginRequestDto();
        loginRequest.setLogin(TestConstants.TEST_LOGIN);
        loginRequest.setPassword(TestConstants.TEST_PASSWORD);
    }
}
