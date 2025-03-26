package asset.spy.auth.service.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends CustomException {
    public InvalidRefreshTokenException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
