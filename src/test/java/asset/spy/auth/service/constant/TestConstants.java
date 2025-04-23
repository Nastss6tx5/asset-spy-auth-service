package asset.spy.auth.service.constant;

import java.time.LocalDate;

public class TestConstants {
    public static final String TEST_LOGIN = "testUser";
    public static final String TEST_USERNAME = "testUser";
    public static final String TEST_PASSWORD = "password123";
    public static final String TEST_DESCRIPTION = "testDescription";
    public static final LocalDate TEST_DATE_OF_BIRTH = LocalDate.of(1990, 1, 1);
    public static final String TEST_USER_AGENT = "mobile-ios";

    public static final String LOGIN_VALIDATION = "Login must be between 2 and 50 characters";
    public static final String USERNAME_VALIDATION = "Username must be between 2 and 50 characters";
    public static final String PASSWORD_VALIDATION = "Password must be between 6 and 100 characters";
    public static final String DESCRIPTION_VALIDATION = "Description must be between 6 and 200 characters";
    public static final String DATE_OF_BIRTH_VALIDATION = "Date of birth must be in the past";
    public static final String MUST_NOT_BE_BLANK = "must not be blank";
}
