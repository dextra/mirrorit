package mirrorit;

public class MirroritException extends RuntimeException {

	private static final long serialVersionUID = 1578430119913938844L;

	public MirroritException(String message, Throwable cause) {
		super(message, cause);
	}

	public MirroritException(String message) {
		super(message);
	}

	public MirroritException(Throwable cause) {
		super(cause);
	}

}
