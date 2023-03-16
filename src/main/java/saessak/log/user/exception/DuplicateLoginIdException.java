package saessak.log.user.exception;

public class DuplicateLoginIdException extends RuntimeException {

    public DuplicateLoginIdException(String message) {
        super(message);
    }

}
