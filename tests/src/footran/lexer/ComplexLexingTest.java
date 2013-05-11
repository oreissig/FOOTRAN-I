package footran.lexer;

import static footran.lexer.TokenType.*;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class ComplexLexingTest {

	@Test
	public void testSubscripts() {
		// manual page 11
		String[] inputs = {
				"MU+2",		// 1
				"MU-2",		// 2
				"5*J",		// 3
				"5*J+2",	// 4
				"5*J-2" };	// 5
		Lexer l = LexUtil.create(inputs);

		TokenType[] types1 = { VAR_INT, PLUS, CONST_INT };
		assertStatement(types1, l.next());

		TokenType[] types2 = { VAR_INT, MINUS, CONST_INT };
		assertStatement(types2, l.next());

		TokenType[] types3 = { CONST_INT, MUL, VAR_INT };
		assertStatement(types3, l.next());

		TokenType[] types4 = { CONST_INT, MUL, VAR_INT, PLUS, CONST_INT };
		assertStatement(types4, l.next());

		TokenType[] types5 = { CONST_INT, MUL, VAR_INT, MINUS, CONST_INT };
		assertStatement(types5, l.next());
	}

	public void testSubscriptedVariable() {
		// manual page 11
		// TODO
	}

	@Test
	public void testFunctions() {
		// manual page 12
		// TODO
	}

	@Test
	public void testExpressions() {
		// manual page 14-15
		// TODO
	}

	@Test
	public void testArithmeticFormulas() {
		// manual page 16
		// TODO
	}

	@Test
	public void testFunctionStatements() {
		// manual page 17
		// TODO
	}

	private void assertStatement(TokenType[] expected, Statement actual) {
		Iterator<Token> tokens = actual.getTokens().iterator();
		for (TokenType type : expected) {
			Token t = tokens.next();
			assertEquals("@" + t.getLineNumber() + ":" + t.getOffset(),
					type, t.getType());
		}
		assertFalse("more tokens than expected", tokens.hasNext());
	}
}
