package footran.reader;

class CardImpl implements Card {
	private static final int NO_CONTINUATION = '0';

	private final int lineNo;
	private final boolean comment;
	private final char continuation;
	private final Integer number;
	private final String statement;
	private final String ident;

	/**
	 * Creates a new punched card representation.
	 * 
	 * @param line
	 *            to parse
	 * @param lineNo
	 *            number of line (card)
	 */
	public CardImpl(String line, int lineNo) {
		// TODO better error messages including lineNo and offset
		if (line.length() > 80)
			throw new CardException("line too long");
		if (line.contains("\n"))
			throw new CardException("statements can only have one line");

		this.lineNo = lineNo;
		line = line.toUpperCase();

		if (line.length() >= 1 && line.charAt(0) == 'C')
			comment = true;
		else
			comment = false;

		if (line.length() >= 5) {
			int start = comment ? 1 : 0;
			String numTag = line.substring(start, 5).trim();
			if (numTag.isEmpty())
				number = null;
			else {
				number = Integer.parseInt(numTag);
				if (number < 0)
					throw new CardException("statement number must be positive");
				else if (number >= 32768)
					throw new CardException("statement number must be smaller than 32768");
			}
		} else {
			number = null;
		}

		if (line.length() >= 6) {
			char c = line.charAt(5);
			if (Character.isWhitespace(c))
				continuation = NO_CONTINUATION;
			else
				continuation = c;
		} else {
			continuation = NO_CONTINUATION;
		}

		if (line.length() > 6) {
			int remain;
			if (line.length() >= 72)
				remain = 72;
			else
				remain = line.length();
			statement = line.substring(STATEMENT_OFFSET, remain);
		} else {
			statement = null;
		}

		if (line.length() > 72) {
			ident = line.substring(72, line.length());
		} else {
			ident = null;
		}
	}

	@Override
	public int getLineNumber() {
		return lineNo;
	}

	@Override
	public boolean isComment() {
		return comment;
	}

	@Override
	public boolean hasStatementNumber() {
		return number != null;
	}

	@Override
	public int getStatementNumber() {
		return number;
	}

	@Override
	public boolean isContinuation() {
		return continuation != NO_CONTINUATION;
	}

	@Override
	public char getContinuation() {
		return continuation;
	}

	@Override
	public boolean hasStatement() {
		return statement != null && !statement.trim().isEmpty();
	}

	@Override
	public String getStatement() {
		return statement;
	}

	@Override
	public boolean hasIdentifier() {
		return ident != null && !ident.isEmpty();
	}

	@Override
	public String getIdentifier() {
		return ident;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getLineNumber()).append(": ");
		if (isComment())
			sb.append("Comment: ");
		if (hasStatementNumber())
			sb.append("Number ").append(getStatementNumber()).append(": ");
		if (isContinuation()) {
			sb.append("Continuation ").append(getContinuation()).append(": ");
		}
		if (hasStatement())
			sb.append("\"").append(getStatement()).append("\"");
		if (hasIdentifier())
			sb.append(" (").append(getIdentifier()).append(")");

		return sb.toString();
	}

	@Override
	public int compareTo(Card other) {
		return this.getLineNumber() - other.getLineNumber();
	}
}
