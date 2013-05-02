package fortran.lexer;

public class LexicalException extends RuntimeException {
	private static final long serialVersionUID = 2809803236048243617L;

	public LexicalException() {
		super();
	}

	public LexicalException(String msg) {
		super(msg);
	}

	public LexicalException(Throwable cause) {
		super(cause);
	}

	public LexicalException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
