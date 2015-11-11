package footran.lexer;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import footran.reader.Card;
import footran.reader.TestCard;

public class StatementHandlingTest {

	@Test
	public void testStatementNumbers() {
		Card[] cards = {
				// 1
				TestCard.builder().build(),
				// 2
				TestCard.builder().setStatementNumber(2).build() };
		Lexer l = LexUtil.create(cards);
		
		// 1
		Statement s = l.next();
		assertFalse(s.hasStatementNumber());
		// 2
		s = l.next();
		assertTrue(s.hasStatementNumber());
		assertEquals(2, s.getStatementNumber());
	}

	@Test
	public void testComments() {
		/*
		 * identify cards by statement number to make sure the correct card got
		 * ignored
		 */
		int num = 1;
		Card[] cards = {
				// 1
				TestCard.builder().setStatementNumber(num++).build(),
				// comment
				TestCard.builder().setStatementNumber(num++).setComment().build(),
				// 2
				TestCard.builder().setStatementNumber(num++).build() };
		Lexer l = LexUtil.create(cards);

		// 1
		assertEquals(1, l.next().getStatementNumber());
		// 2
		assertEquals(3, l.next().getStatementNumber());
		assertFalse(l.hasNext());
	}

	@Test
	public void testContinuations() {
		int num = 1;
		Card[] cards = {
				// 1
				TestCard.builder().setStatementNumber(num++).build(),
				// 2
				TestCard.builder().setStatementNumber(num++).build(),
				TestCard.builder().setContinuation('1').build(),
				TestCard.builder().setComment().build(),
				TestCard.builder().setContinuation('2').build(),
				// 3
				TestCard.builder().setStatementNumber(num++).build(), };
		Lexer l = LexUtil.create(cards);

		// 1
		assertEquals(1, l.next().getStatementNumber());
		// 2
		Statement s = l.next();
		assertEquals(2, s.getStatementNumber());
		assertEquals(3, s.getCards().size());
		Iterator<Card> c = s.getCards().iterator();
		assertFalse(c.next().isContinuation());
		assertEquals('1', c.next().getContinuation());
		assertEquals('2', c.next().getContinuation());
		// 3
		assertEquals(3, l.next().getStatementNumber());
		assertFalse(l.hasNext());
	}

	@Test
	public void testLineNumbers() {
		int line = 1;
		Card[] cards = {
				// 1
				TestCard.builder().setLineNumber(line++).setStatement("+").build(),
				TestCard.builder().setLineNumber(line++).setComment().build(),
				// 2
				TestCard.builder().setLineNumber(line++).setStatement("+").build(),
				TestCard.builder().setLineNumber(line++).setStatement("+").setContinuation('1').build(),
				// 3
				TestCard.builder().setLineNumber(line++).setStatement("+").build(), };
		Lexer l = LexUtil.create(cards);

		// 1
		List<Token> t1 = l.next().getTokens();
		assertEquals(1, t1.get(0).getLineNumber());
		// 2
		List<Token> t2 = l.next().getTokens();
		assertEquals(3, t2.get(0).getLineNumber());
		assertEquals(4, t2.get(1).getLineNumber());
		// 3
		List<Token> t3 = l.next().getTokens();
		assertEquals(5, t3.get(0).getLineNumber());
	}
}
