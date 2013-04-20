package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import fortran.reader.Card;
import fortran.reader.CardException;
import fortran.reader.CardImpl;

public class CardTest {
	@Test
	public void testConstrains() {
		// more than one line in input string
		String line1 = "C      \nC      ";
		try {
			new CardImpl(line1);
			fail();
		} catch (CardException ex) {
		}

		// longer than 80 chars
		String line2 = "C        1         2         3         4         5         6         7         81";
		try {
			new CardImpl(line2);
			fail();
		} catch (CardException ex) {
		}
	}

	@Test
	public void testComment() {
		String line1 = "C";
		Card s1 = new CardImpl(line1);
		assertTrue(s1.isComment());

		String line2 = "c  20XTestfoo";
		Card s2 = new CardImpl(line2);
		assertTrue(s2.isComment());

		String line3 = "";
		Card s3 = new CardImpl(line3);
		assertFalse(s3.isComment());

		String line4 = "  10 barbaz";
		Card s4 = new CardImpl(line4);
		assertFalse(s4.isComment());
	}

	@Test
	public void testContinuation() {
		String line1 = "      ";
		Card s1 = new CardImpl(line1);
		assertFalse(s1.isContinuation());
		if (s1.getContinuation() != '0' && s1.getContinuation() != ' ')
			fail();

		String line2 = "";
		Card s2 = new CardImpl(line2);
		assertFalse(s2.isContinuation());

		String line3 = "     X";
		Card s3 = new CardImpl(line3);
		assertTrue(s3.isContinuation());
		assertEquals('X', s3.getContinuation());

		String line4 = "     0";
		Card s4 = new CardImpl(line4);
		assertFalse(s4.isContinuation());
	}

	@Test
	public void testIdents() {
		String line1 = "         1         2         3         4         5         6         7 ";
		Card s1 = new CardImpl(line1);
		assertNull(s1.getIdentifier());

		String line2 = "";
		Card s2 = new CardImpl(line2);
		assertNull(s2.getIdentifier());

		String line3 = "         1         2         3         4         5         6         7  12345678";
		Card s3 = new CardImpl(line3);
		assertEquals("12345678", s3.getIdentifier());

		String line4 = "         1         2         3         4         5         6         7    ";
		Card s4 = new CardImpl(line4);
		assertEquals("  ", s4.getIdentifier());
	}

	@Test
	public void testNumbers() {
		String line1 = "C";
		Card s1 = new CardImpl(line1);
		assertFalse(s1.hasNumber());
		
		// lower limit
		String line2 = "    0";
		Card s2 = new CardImpl(line2);
		assertTrue(s2.hasNumber());
		assertEquals(0, s2.getNumber());
		
		String line3 = "C1234a";
		Card s3 = new CardImpl(line3);
		assertTrue(s3.hasNumber());
		assertEquals(1234, s3.getNumber());
		
		// upper limit
		String line4 = "32767";
		Card s4 = new CardImpl(line4);
		assertTrue(s4.hasNumber());
		assertEquals(32767, s4.getNumber());
		
		String line5 = " abcd ";
		try {
			new CardImpl(line5);
			fail();
		} catch (NumberFormatException e) {}
	}

	@Test
	public void testText() {
		String line1 = "     0";
		Card s1 = new CardImpl(line1);
		assertNull("", s1.getStatement());

		String line2 = "";
		Card s2 = new CardImpl(line2);
		assertNull(s2.getStatement());

		String line3 = "     0ABC";
		Card s3 = new CardImpl(line3);
		assertEquals("ABC", s3.getStatement());

		String line4 = "     0   1         2         3         4         5         6         7  IDENT";
		Card s4 = new CardImpl(line4);
		assertEquals("   1         2         3         4         5         6         7  ", s4.getStatement());
	}
}
