package fortran.lexer;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.google.common.collect.UnmodifiableIterator;

import fortran.reader.Card;
import fortran.reader.TestCard;

public class TokenRecognitionTest {
	@Test
	public void testKeywords() {
		Lexer l = create(Keywords.names);
		for (TokenType type : Keywords.set) {
			Statement stmt = l.next();
			assertEquals(1, stmt.getTokens().size());
			Token token = stmt.getTokens().get(0);
			assertEquals(type, token.getType());
		}
	}

	@Test
	public void testLiterals() {
		// TODO test literal tokens
	}

	@Test
	public void testMisc() {
		// TODO test misc. tokens
	}

	private Lexer create(final Iterable<String> input) {
		Iterator<Card> cards = new UnmodifiableIterator<Card>() {
			final Iterator<String> i = input.iterator();

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public Card next() {
				return TestCard.builder().setStatement(i.next()).build();
			}
		};
		return new LexerImpl(cards);
	}
}
