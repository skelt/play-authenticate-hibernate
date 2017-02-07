package exceptions;

public class PlayAuthHibernateServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PlayAuthHibernateServiceException(String message) {
        super(message);
    }
}