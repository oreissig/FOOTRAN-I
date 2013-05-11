package footran.lexer;

import static footran.lexer.TokenType.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class TokenRecognitionTest {
	@Test
	public void testKeywords() {
		// manual page 55
		Lexer l = LexUtil.create(Keywords.names);
		for (TokenType type : Keywords.set) {
			Statement stmt = l.next();
			assertEquals(type.name(), 1, stmt.getTokens().size());
			Token token = stmt.getTokens().get(0);
			assertEquals(type.name(), type, token.getType());
		}
	}

	@Test
	public void testIntConstants() {
		// manual page 9
		// leave out signs in the examples, they are not part of the token
		String[] inputs = {
				"3",
				"1",
				"28987" };
		Lexer l = LexUtil.create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(s, 1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(s, CONST_INT, t.getType());
			assertEquals(s, s, t.getText());
		}
	}

	@Test
	public void testFloatConstants() {
		// manual page 9
		// leave out signs in the examples, they are not part of the token
		String[] inputs = {
				"17.",
				"5.0",
				".0003",
				"5.0E3",
				"5.0E+3",
				"5.0E-7" };
		Lexer l = LexUtil.create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(s, 1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(s, CONST_FLOAT, t.getType());
			assertEquals(s, s, t.getText());
		}
	}

	@Test
	public void testIntVariables() {
		// manual page 10
		String[] inputs = {
				"I",
				"M2",
				"JOBN0" };
		Lexer l = LexUtil.create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(s, 1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(s, VAR_INT, t.getType());
			assertEquals(s, s, t.getText());
		}
	}

	@Test
	public void testFloatVariables() {
		// manual page 10
		String[] inputs = {
				"A",
				"B7",
				"DELTA" };
		Lexer l = LexUtil.create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(s, 1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(s, VAR_FLOAT, t.getType());
			assertEquals(s, s, t.getText());
		}
	}

	@Test
	public void testIntFunctions() {
		// manual page 12-13
		String[] inputs = {
				"XTANF",
				"XSIN0F",
				"XSIN1F" };
		Lexer l = LexUtil.create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(s, 1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(s, TokenType.FUNC_INT, t.getType());
			assertEquals(s, s, t.getText());
		}
	}

	@Test
	public void testFloatFunctions() {
		// manual page 12-13
		String[] inputs = {
				"SINF",
				"SOMEF",
				"SQRTF",
				"SIN0F",
				"SIN1F" };
		Lexer l = LexUtil.create(inputs);
		for (String s : inputs) {
			List<Token> tokens = l.next().getTokens();
			assertEquals(s, 1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(s, TokenType.FUNC_FLOAT, t.getType());
			assertEquals(s, s, t.getText());
		}
	}

	@Test
	public void testOpsAndMisc() {
		// manual page 14-15
		Map<String, TokenType> inputs = ImmutableMap.<String, TokenType> builder()
				.put("(", PAREN1)
				.put(")", PAREN2)
				.put(",", COMMA)
				.put("=", EQUALS)
				.put("+", PLUS)
				.put("-", MINUS)
				.put("*", MUL)
				.put("**", EXP)
				.put("/", DIV)
				.build();

		Lexer l = LexUtil.create(inputs.keySet());
		for (Entry<String, TokenType> e : inputs.entrySet()) {
			String s = e.getKey();
			TokenType type = e.getValue();
			List<Token> tokens = l.next().getTokens();
			assertEquals(s, 1, tokens.size());
			Token t = tokens.get(0);
			assertEquals(s, type, t.getType());
			assertEquals(s, s, t.getText());
		}
	}
}
