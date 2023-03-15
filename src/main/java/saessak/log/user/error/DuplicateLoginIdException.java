package saessak.log.user.error;

public class DuplicateLoginIdException extends RuntimeException {

    public DuplicateLoginIdException(String message) {
        super(message);
    }

}
