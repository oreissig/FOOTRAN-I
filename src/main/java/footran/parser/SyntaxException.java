package footran.parser;

public class SyntaxException extends RuntimeException {
	private static final long serialVersionUID = -4570916331414704657L;

	public SyntaxException() {
		super();
	}

	public SyntaxException(String msg) {
		super(msg);
	}

	public SyntaxException(Throwable cause) {
		super(cause);
	}

	public SyntaxException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
