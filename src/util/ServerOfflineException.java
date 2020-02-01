package util;

public class ServerOfflineException extends Exception {
    public ServerOfflineException(String message) {
        super(message);
    }

    public ServerOfflineException() {
        super();
    }
}
