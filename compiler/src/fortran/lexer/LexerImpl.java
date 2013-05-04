package fortran.lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fortran.reader.Card;

class LexerImpl extends StatementHandler {

	private Card current;
	private int position;
	private Character c;

	public LexerImpl(Iterable<Card> cards) {
		super(cards);
	}

	public LexerImpl(Iterator<Card> cards) {
		super(cards);
	}

	@Override
	protected List<Literal> lex(Card card) {
		List<Literal> literals = new ArrayList<>();
		position = -1;
		c = null;
		current = card;
		nextChar();

		Literal l = start();
		while (l != null) {
			literals.add(l);
			l = start();
		}

		return literals;
	}

	private Literal start() {
		while (c != null && Character.isWhitespace(c))
			next();

		// TODO implement lexer
		return null;
	}

	private char nextChar() {
		return c = current.getStatement().charAt(++position);
	}

	private char peekChar() {
		return current.getStatement().charAt(position+1);
	}

	private boolean expect(String toBeExpected) {
		String stmt = current.getStatement();
		// enough chars left?
		if ((stmt.length() - position) < toBeExpected.length())
			return false;
		else
			return stmt.substring(position).startsWith(toBeExpected);
	}

	private Literal createLiteral(LiteralType type, String text) {
		return new LiteralImpl(type, current.getLineNumber(),
				Card.STATEMENT_OFFSET + position, text);
	}
}
