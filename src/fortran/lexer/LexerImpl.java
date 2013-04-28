package fortran.lexer;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;

import fortran.reader.Card;

public class LexerImpl extends AbstractIterator<Literal> implements Lexer {

	private final Iterator<Card> cards;
	private Card current = null;
	private int position = -1;

	/**
	 * Creates a new Lexer based on the given
	 * stream of {@link Card}s.
	 * 
	 * @param cards to lex
	 */
	public LexerImpl(Iterator<Card> cards) {
		this.cards = cards;
	}

	/**
	 * Creates a new Lexer based on the given
	 * stream of {@link Card}s.
	 * 
	 * @param cards to lex
	 */
	public LexerImpl(Iterable<Card> cards) {
		this(cards.iterator());
	}

	@Override
	protected Literal computeNext() {
		if (current == null) {
			if (cards.hasNext()) {
				current = cards.next();
				position = 0;
			} else {
				return endOfData();
			}
		}
		
		
		
		// TODO
		return endOfData();
	}
}
