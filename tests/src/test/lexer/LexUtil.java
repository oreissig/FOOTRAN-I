package footran.lexer;

import java.util.Arrays;
import java.util.Iterator;

import com.google.common.collect.UnmodifiableIterator;

import footran.reader.Card;
import footran.reader.TestCard;

public class LexUtil {
	// forbid instantiation
	private LexUtil() {
	}

	/**
	 * Create a lexer for the given input lines.
	 * 
	 * @param lines
	 *            to lex
	 * @return lexer
	 */
	public static Lexer create(final Iterable<String> lines) {
		Iterator<Card> cards = new UnmodifiableIterator<Card>() {
			private final Iterator<String> i = lines.iterator();
			private int lineNo = 1;

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public Card next() {
				return TestCard.builder()
						.setStatement(i.next())
						.setLineNumber(lineNo++)
						.build();
			}
		};
		return create(cards);
	}

	/**
	 * Create a lexer for the given input lines.
	 * 
	 * @param lines
	 *            to lex
	 * @return lexer
	 */
	public static Lexer create(final String[] lines) {
		return create(Arrays.asList(lines));
	}

	/**
	 * Create a lexer for the given input listing.
	 * 
	 * @param input
	 *            to lex
	 * @return lexer
	 */
	public static Lexer create(final String input) {
		return create(input.split("\n"));
	}

	/**
	 * Create a lexer for the given cards.
	 * 
	 * @param cards
	 *            to lex
	 * @return lexer
	 */
	public static Lexer create(final Card[] cards) {
		return create(Arrays.asList(cards).iterator());
	}

	/**
	 * Create a lexer for the given cards.
	 * 
	 * @param cards
	 *            to lex
	 * @return lexer
	 */
	public static Lexer create(final Iterator<Card> cards) {
		return new LexerImpl(cards);
	}
}
