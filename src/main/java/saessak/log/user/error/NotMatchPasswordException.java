package saessak.log.user.error;

public class NotMatchPasswordException extends RuntimeException {

    public NotMatchPasswordException(String message) {
        super(message);
    }

}
