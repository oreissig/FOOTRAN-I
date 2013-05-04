package fortran.lexer;

class LiteralImpl implements Literal {
	private final LiteralType type;
	private final int lineNumber;
	private final int offset;
	private final String text;

	public LiteralImpl(LiteralType type, int lineNumber, int offset, String text) {
		this.type = type;
		this.lineNumber = lineNumber;
		this.offset = offset;
		this.text = text;
	}

	@Override
	public LiteralType getType() {
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
