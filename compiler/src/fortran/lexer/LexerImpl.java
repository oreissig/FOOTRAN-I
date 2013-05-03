package fortran.lexer;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import fortran.reader.Card;

class LexerImpl extends AbstractIterator<Statement> implements Lexer {

	private final PeekingIterator<Card> cards;
	private Card card = null;
	private int position = -1;

	/**
	 * Creates a new Lexer based on the given stream of {@link Card}s.
	 * 
	 * @param cards
	 *            to lex
	 */
	public LexerImpl(Iterator<Card> cards) {
		if (cards instanceof PeekingIterator)
			this.cards = (PeekingIterator<Card>) cards;
		else
			this.cards = Iterators.peekingIterator(cards);
	}

	/**
	 * Creates a new Lexer based on the given {@link Card}s.
	 * 
	 * @param cards
	 *            to lex
	 */
	public LexerImpl(Iterable<Card> cards) {
		this(cards.iterator());
	}

	@Override
	protected Statement computeNext() {
		final Statement stmt = new StatementImpl();
		
		if (card == null) {
			if (!nextCard())
				endOfData();
		}
		
		// TODO
		return endOfData();
	}

	/**
	 * Reads the next non-comment card.
	 * 
	 * @return true on success, false if no more cards
	 */
	private boolean nextCard() {
		position = 0;
		do {
			if (cards.hasNext()) {
				card = cards.next();
			} else {
				return false;
			}
		} while (card.isComment());
		return true;
	}

	private boolean expect(String toBeExpected) {
		String stmt = card.getStatement();
		// enough chars left?
		if ((stmt.length()-position) < toBeExpected.length())
			return false;
		else
			return stmt.substring(position).startsWith(toBeExpected);
	}
}
