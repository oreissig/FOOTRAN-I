package fortran.lexer;

import static fortran.lexer.TokenType.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
	public void testIntConstants() {
		// manual page 9
		String[] inputs = {"3", "+1", "-28987"};
		Lexer l = create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(CONST_INT, t.getType());
			assertEquals(s, t.getText());
		}
	}

	@Test
	public void testFloatConstants() {
		// manual page 9
		String[] inputs = {"17.", "5.0", "-.0003", "5.0E3", "5.0E+3", "5.0E-7"};
		Lexer l = create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(CONST_INT, t.getType());
			assertEquals(s, t.getText());
		}
	}

	@Test
	public void testIntVariables() {
		// manual page 10
		String[] inputs = {"I", "M2", "JOBN0"};
		Lexer l = create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(VAR_INT, t.getType());
			assertEquals(s, t.getText());
		}
	}

	@Test
	public void testFloatVariables() {
		// manual page 10
		String[] inputs = {"A", "B7", "DELTA"};
		Lexer l = create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(VAR_FLOAT, t.getType());
			assertEquals(s, t.getText());
		}
	}

	@Test
	public void testIntFunctions() {
		// manual page 12
		String[] inputs = {"SINF", "SOMEF", "SQRTF", "SIN0F", "SIN1F"};
		Lexer l = create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(TokenType.FUNC_INT, t.getType());
			assertEquals(s, t.getText());
		}
	}

	public void testFloatFunctions() {
		String[] inputs = {"XTANF", "XSIN0F", "XSIN1F"};
		Lexer l = create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(TokenType.FUNC_FLOAT, t.getType());
			assertEquals(s, t.getText());
		}
	}

	@Test
	public void testMisc() {
		// TODO test misc. tokens
		fail();
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
	

	private Lexer create(final String[] input) {
		return create(Arrays.asList(input));
	}
}
