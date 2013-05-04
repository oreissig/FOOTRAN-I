package fortran.lexer;

import java.util.Iterator;
import java.util.List;

import fortran.reader.Card;

class LexerImpl extends StatementHandler {

	private Card current;
	private int position = -1;

	public LexerImpl(Iterable<Card> cards) {
		super(cards);
	}

	public LexerImpl(Iterator<Card> cards) {
		super(cards);
	}

	@Override
	protected List<Literal> lex(Card card) {
		position = 0;
		current = card;
		
		// TODO Auto-generated method stub
		return null;
	}

	private boolean expect(String toBeExpected) {
		String stmt = current.getStatement();
		// enough chars left?
		if ((stmt.length() - position) < toBeExpected.length())
			return false;
		else
			return stmt.substring(position).startsWith(toBeExpected);
	}
}
