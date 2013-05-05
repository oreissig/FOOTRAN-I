package fortran.lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fortran.reader.Card;

class LexerImpl extends StatementHandler {

	private String stmt;
	private int lineNo;
	private int offset;
	private Character c;

	public LexerImpl(Iterable<Card> cards) {
		super(cards);
	}

	public LexerImpl(Iterator<Card> cards) {
		super(cards);
	}

	@Override
	protected List<Token> lex(Card card) {
		List<Token> tokens = new ArrayList<>();
		offset = -1;
		c = null;
		stmt = card.getStatement();
		lineNo = card.getLineNumber();
		nextChar();

		Token l = start();
		while (l != null) {
			tokens.add(l);
			l = start();
		}

		return tokens;
	}

	private Token start() {
		while (c != null && Character.isWhitespace(c))
			next();

		// TODO implement lexer
		return null;
	}

	private char nextChar() {
		return c = stmt.charAt(++offset);
	}

	private char peekChar() {
		return stmt.charAt(offset+1);
	}

	private boolean expect(String toBeExpected) {
		// enough chars left?
		if ((stmt.length() - offset) < toBeExpected.length())
			return false;
		else
			return stmt.substring(offset).startsWith(toBeExpected);
	}

	private Token createToken(TokenType type, String text) {
		return new TokenImpl(type, lineNo,
				Card.STATEMENT_OFFSET + offset, text);
	}
}
