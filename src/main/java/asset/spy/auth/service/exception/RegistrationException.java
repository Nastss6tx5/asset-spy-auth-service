package asset.spy.auth.service.exception;

import org.springframework.http.HttpStatus;

public class RegistrationException extends CustomException {

    public RegistrationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
