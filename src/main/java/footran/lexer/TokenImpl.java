package footran.lexer;

import static footran.lexer.TokenType.*;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

class TokenImpl implements Token {
	private static final Set<TokenType> printTextFor =
			Collections.unmodifiableSet(
					EnumSet.range(VAR_INT, CONST_FLOAT));
	
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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Token))
			return false;

		Token o = (Token) obj;
		return getType().equals(o.getType())
				&& getLineNumber() == o.getLineNumber()
				&& getOffset() == o.getOffset()
				&& getText().equals(o.getText());
	}

	@Override
	public int compareTo(Token o) {
		if (getLineNumber() == o.getLineNumber())
			return o.getLineNumber() - getLineNumber();
		else
			return o.getOffset() - o.getOffset();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(type.name());
		if (printTextFor.contains(type)) {
			sb.append(" \"")
			  .append(text)
			  .append('\"');
		}
		sb.append(" @")
		  .append(lineNumber)
		  .append(':')
		  .append(offset);
		return sb.toString();
	}
}
