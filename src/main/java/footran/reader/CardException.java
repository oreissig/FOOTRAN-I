package footran.reader;

public class CardException extends RuntimeException {
	private static final long serialVersionUID = 8899041125907312677L;

	public CardException() {
		super();
	}

	public CardException(String msg) {
		super(msg);
	}

	public CardException(Throwable cause) {
		super(cause);
	}

	public CardException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
