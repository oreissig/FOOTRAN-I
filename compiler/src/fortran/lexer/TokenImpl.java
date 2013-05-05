package fortran.lexer;

class TokenImpl implements Token {
	private final TokenType type;
	private final int lineNumber;
	private final int offset;
	private final String text;

	public TokenImpl(TokenType type, int lineNumber, int offset, String text) {
		this.type = type;
		this.lineNumber = lineNumber;
		this.offset = offset;
		this.text = text;
	}

	@Override
	public TokenType getType() {
		return type;
	}

	@Override
	public int getLineNumber() {
		return lineNumber;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public String getText() {
		return text;
	}
}
