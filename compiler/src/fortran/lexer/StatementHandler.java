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
			// EOF
			return endOfData();
		if (current.isContinuation())
			log.error("statement at line {} starts with continuation {}",
					current.getLineNumber(), current.getContinuation());
		lex(current, stmt);

		// check for continuations
		char lastContinuation = '0';
		while (peekCard() != null && peekCard().isContinuation()) {
			current = nextCard();
			// check sanity of continuation mark
			if (!Character.isDigit(current.getContinuation()))
				log.warn("continuations are only specified for values 0-9, got {} instead at line {}",
						 current.getContinuation(), current.getLineNumber());
			else if (current.getContinuation() != lastContinuation+1)
				log.warn("Expected continuation {}, got {} at line {}",
						 (char)(lastContinuation+1), current.getContinuation(),
						 current.getLineNumber());
			lastContinuation = current.getContinuation();
			
			lex(current, stmt);
		}

		return stmt.build();
	}

	private void lex(Card current, StatementBuilder stmt) {
		stmt.addCard(current);
		for (Token l : lex(current))
			stmt.addToken(l);
	}

	/**
	 * Does the lexing for a given card.
	 * 
	 * @param card 
	 *            with statement to lex
	 * @return list of tokens
	 */
	protected abstract List<Token> lex(Card card);

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
