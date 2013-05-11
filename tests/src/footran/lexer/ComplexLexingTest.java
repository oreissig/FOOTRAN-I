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
				"MU+2",
				"MU-2",
				"5*J",
				"5*J+2",
				"5*J-2" };
		Lexer l = LexUtil.create(inputs);

		// MU+2
		assertStatement(new TokenType[]
				{ VAR_INT, PLUS, CONST_INT },
				l.next());

		// MU-2
		assertStatement(new TokenType[]
				{ VAR_INT, MINUS, CONST_INT },
				l.next());

		// 5*J
		assertStatement(new TokenType[]
				{ CONST_INT, MUL, VAR_INT },
				l.next());

		// 5*J+2
		assertStatement(new TokenType[]
				{ CONST_INT, MUL, VAR_INT, PLUS, CONST_INT },
				l.next());

		// 5*J-2
		assertStatement(new TokenType[]
				{ CONST_INT, MUL, VAR_INT, MINUS, CONST_INT },
				l.next());
	}

	public void testSubscriptedVariable() {
		// manual page 11
		String[] inputs = {
				"A(I)",
				"K(3)",
				"BETA(5*J-2,K+2,L)" };
		Lexer l = LexUtil.create(inputs);

		// A(I)
		assertStatement(new TokenType[]
				{ VAR_FLOAT, PAREN1, VAR_INT, PAREN2 },
				l.next());

		// K(3)
		assertStatement(new TokenType[]
				{ VAR_INT, PAREN1, VAR_INT, PAREN2 },
				l.next());

		// BETA(5*J-2,K+2,L)
		assertStatement(new TokenType[]
				{ VAR_FLOAT, PAREN1, CONST_INT, MUL, VAR_INT, MINUS, CONST_INT,
					COMMA, VAR_INT, PLUS, CONST_INT, COMMA, VAR_INT, PAREN2 },
				l.next());
	}

	@Test
	public void testFunctions() {
		// manual page 12
		String[] inputs = {
				"SINF(A+B)",
				"SOMEF(X,Y)",
				"SQRTF(SINF(A))",
				"XTANF(3.*X)" };
		
		Lexer l = LexUtil.create(inputs);

		// SINF(A+B)
		assertStatement(new TokenType[]
				{ FUNC_FLOAT, PAREN1, VAR_FLOAT, PLUS, VAR_FLOAT , PAREN2 },
				l.next());

		// SOMEF(X,Y)
		assertStatement(new TokenType[]
				{ FUNC_FLOAT, PAREN1, VAR_FLOAT, COMMA, VAR_FLOAT , PAREN2 },
				l.next());

		// SQRTF(SINF(A))
		assertStatement(new TokenType[]
				{ FUNC_FLOAT, PAREN1, FUNC_FLOAT, PAREN1, VAR_FLOAT , PAREN2 ,
					PAREN2 },
				l.next());

		// XTANF(3.*X)
		assertStatement(new TokenType[]
				{ FUNC_INT, PAREN1, CONST_FLOAT, MUL, VAR_FLOAT , PAREN2 },
				l.next());
	}

	@Test
	public void testArithmeticFormulas() {
		// manual page 16
		String[] inputs = {
				"I=I+1",
				"A=MAX1F(SINF(B), COSF(B))",
				"A=3.0*B",
				"A(I)=B(I)+SINF(C(I))" };
		Lexer l = LexUtil.create(inputs);

		// I=I+1
		assertStatement(new TokenType[]
				{ VAR_INT, EQUALS, VAR_INT, PLUS, CONST_INT },
				l.next());

		// A=MAX1F(SINF(B), COSF(B))
		assertStatement(new TokenType[]
				{ VAR_FLOAT, EQUALS, FUNC_FLOAT, PAREN1, FUNC_FLOAT, PAREN1,
					VAR_FLOAT, PAREN2, COMMA, FUNC_FLOAT, PAREN1, VAR_FLOAT,
					PAREN2, PAREN2 },
				l.next());

		// A=3.0*B
		assertStatement(new TokenType[]
				{ VAR_FLOAT, EQUALS, CONST_FLOAT, MUL, VAR_FLOAT },
				l.next());

		// A(I)=B(I)+SINF(C(I))
		assertStatement(new TokenType[]
				{ VAR_FLOAT, PAREN1, VAR_INT, PAREN2, EQUALS, VAR_FLOAT, PAREN1,
					VAR_INT, PAREN2, PLUS, FUNC_FLOAT, PAREN1, VAR_FLOAT,
					PAREN1, VAR_INT, PAREN2, PAREN2 },
				l.next());
	}

	@Test
	public void testFunctionStatements() {
		// manual page 17
		String[] inputs = {
				"FIRSTF(X) = A*X + B",
				"SECONDF(X,B) = A*X + B",
				"THIRDF(D) = FIRSTF(E)/D",
				"FOURTHF(F,G) = SECONDF(F,THIRDF(G))",
				"FIFTHF(I,A) = 3.0*A**I",
				"SIXTHF(J) = J + K",
				"XSIXTHF(J) = J + K" };
		Lexer l = LexUtil.create(inputs);

		// FIRSTF(X) = A*X + B
		assertStatement(new TokenType[]
				{ FUNC_FLOAT, PAREN1, VAR_FLOAT, PAREN2, EQUALS, VAR_FLOAT, MUL,
					VAR_FLOAT, PLUS, VAR_FLOAT },
				l.next());

		// SECONDF(X,B) = A*X + B
		assertStatement(new TokenType[]
				{ FUNC_FLOAT, PAREN1, VAR_FLOAT, COMMA, VAR_FLOAT, PAREN2,
					EQUALS, VAR_FLOAT, MUL, VAR_FLOAT, PLUS, VAR_FLOAT },
				l.next());

		// THIRDF(D) = FIRSTF(E)/D
		assertStatement(new TokenType[]
				{ FUNC_FLOAT, PAREN1, VAR_FLOAT, PAREN2, EQUALS, FUNC_FLOAT,
					PAREN1, VAR_FLOAT, PAREN2, DIV, VAR_FLOAT },
				l.next());

		// FOURTHF(F,G) = SECONDF(F,THIRDF(G))
		assertStatement(new TokenType[]
				{ FUNC_FLOAT, PAREN1, VAR_FLOAT, COMMA, VAR_FLOAT, PAREN2,
					EQUALS, FUNC_FLOAT, PAREN1, VAR_FLOAT, COMMA, FUNC_FLOAT,
					PAREN1, VAR_FLOAT, PAREN2, PAREN2 },
				l.next());

		// FIFTHF(I,A) = 3.0*A**I
		assertStatement(new TokenType[]
				{ FUNC_FLOAT, PAREN1, VAR_INT, COMMA, VAR_FLOAT, PAREN2, EQUALS,
					CONST_FLOAT, MUL, VAR_FLOAT, EXP, VAR_INT },
				l.next());

		// SIXTHF(J) = J + K
		assertStatement(new TokenType[]
				{ FUNC_FLOAT, PAREN1, VAR_INT, PAREN2, EQUALS, VAR_INT, PLUS,
					VAR_INT },
				l.next());

		// XSIXTHF(J) = J + K
		assertStatement(new TokenType[]
				{ FUNC_INT, PAREN1, VAR_INT, PAREN2, EQUALS, VAR_INT, PLUS,
					VAR_INT },
				l.next());
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
