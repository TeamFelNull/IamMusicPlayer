package red.felnull.imp.throwable;

public class InvalidIdentifierException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidIdentifierException(String name) {
        super(name);
    }
}
