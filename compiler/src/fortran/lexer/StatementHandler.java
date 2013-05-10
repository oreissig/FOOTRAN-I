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
	int lineNo = -1;

	/**
	 * Creates a new StatementHandler based on the given stream of {@link Card}s.
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
			error("statement starts with continuation " + current.getContinuation(),
				  Card.CONTINUATION_OFFSET);
		lex(current, stmt);

		// check for continuations
		char lastContinuation = '0';
		while (peekCard() != null && peekCard().isContinuation()) {
			current = nextCard();
			// check sanity of continuation mark
			if (!Character.isDigit(current.getContinuation()))
				warn("continuations are only specified for values 0-9, got " +
					 current.getContinuation(), Card.CONTINUATION_OFFSET);
			else if (current.getContinuation() != lastContinuation + 1)
				warn("Expected continuation " + (char) (lastContinuation + 1) +
					 ", got " + current.getContinuation(), Card.CONTINUATION_OFFSET);
			lastContinuation = current.getContinuation();

			lex(current, stmt);
		}

		return stmt.build();
	}

	private void lex(Card current, StatementBuilder stmt) {
		stmt.addCard(current);
		List<Token> tokens = lex(current.getStatement());
		for (Token t : tokens)
			stmt.addToken(t);
	}

	/**
	 * Does the lexing for a given card.
	 * 
	 * @param statement
	 *            to lex
	 * @return list of tokens
	 */
	protected abstract List<Token> lex(String statement);

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
		lineNo = next.getLineNumber();
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

	/**
	 * shortcut to issue a warning with a common prefix denoting
	 * line number and the given offset
	 * 
	 * @param msg warning message
	 * @param offset
	 */
	void warn(String msg, int offset) {
		log.warn("@{}:{} {}", lineNo, offset, msg);
	}

	/**
	 * shortcut to issue an error with a common prefix denoting
	 * line number and the given offset
	 * 
	 * @param msg error message
	 * @param offset
	 */
	void error(String msg, int offset) {
		log.error("@{}:{} {}", lineNo, offset, msg);
	}
}
