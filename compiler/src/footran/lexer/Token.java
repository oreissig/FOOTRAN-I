package footran.lexer;

import footran.reader.Card;

/**
 * Tokens represent the basic building blocks, the FORTRAN language is made up
 * of. A {@link Lexer} resolves {@link Card}s into a stream of Tokens.
 * 
 * @author oreissig
 */
public interface Token extends Comparable<Token> {

	/**
	 * Gets the exact type of this token.
	 * 
	 * @return token's type
	 */
	public abstract TokenType getType();

	/**
	 * Gets the line (or card) number, this token has been defined on.
	 * 
	 * @return line number
	 */
	public abstract int getLineNumber();

	/**
	 * Gets the offset from the beginning of the statement section, where this
	 * token begins.
	 * 
	 * @return offset from beginning of statement
	 */
	public abstract int getOffset();

	/**
	 * Gets the token's text representation from the source code.
	 * 
	 * @return token's text
	 */
	public abstract String getText();

}