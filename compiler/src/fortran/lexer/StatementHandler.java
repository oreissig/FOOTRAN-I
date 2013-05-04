package fortran.lexer;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import fortran.reader.Card;

/**
 * This abstract class takes care of all the FORTRAN specific punched card
 * handling (usually not needed in free-form languages), so that the actual
 * lexer only needs to work on the plain statement code.
 * 
 * @author oreissig
 */
abstract class StatementHandler extends AbstractIterator<Statement> implements Lexer {

	static final Logger log = LoggerFactory.getLogger(Lexer.class);

	private final PeekingIterator<Card> cards;

	/**
	 * Creates a new Lexer based on the given stream of {@link Card}s.
	 * 
	 * @param cards
	 *            to lex
	 */
	public StatementHandler(Iterator<Card> cards) {
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
	public StatementHandler(Iterable<Card> cards) {
		this(cards.iterator());
	}

	@Override
	protected Statement computeNext() {
		Card current = null;
		final StatementBuilder stmt = new StatementImpl();

		// read first card
		current = nextCard();
		if (current == null)
			return endOfData();
		// TODO check no continuation usw

		// check for continuations
		while (cards.peek().isContinuation()) {
			// TODO
			lex(current, stmt);
		}

		return endOfData();
	}

	private void lex(Card current, StatementBuilder stmt) {
		stmt.addCard(current);
		List<Literal> literals = lex(current);
		for (Literal l : literals)
			stmt.addLiteral(l);
	}

	protected abstract List<Literal> lex(Card card);

	/**
	 * Reads the next non-comment card.
	 * 
	 * @return next card on success, null if no more cards
	 */
	private Card nextCard() {
		Card next;
		do {
			if (cards.hasNext()) {
				next = cards.next();
			} else {
				return null;
			}
		} while (next.isComment());
		return next;
	}

	/**
	 * Peeks at the next non-comment card without progressing the Iterator to
	 * it.
	 * 
	 * @return peeked Card, null if no more cards
	 */
	private Card peekCard() {
		Card peek;
		while (true) {
			if (!cards.hasNext())
				// EOF
				return null;
			peek = cards.peek();
			if (!peek.isComment())
				// success
				return peek;
			// skip card
			cards.next();
		}
	}
}
