package fortran.lexer;

import fortran.reader.Card;

/**
 * Literals represent the basic building blocks, the FORTRAN language is made up
 * of. A {@link Lexer} resolves {@link Card}s into a stream of Literals.
 * 
 * @author oreissig
 */
public interface Literal {

	/**
	 * Gets the exact type of this literal.
	 * 
	 * @return literal's type
	 */
	public abstract LiteralType getType();

	/**
	 * Gets the Card, this literal has been defined on.
	 * 
	 * @return defining Card
	 */
	public abstract Card getCard();

	/**
	 * Gets the offset from the beginning of the statement section, where this
	 * literal begins.
	 * 
	 * @return offset from beginning of statement
	 */
	public abstract int getOffset();

	/**
	 * Gets the literal's text representation from the source code.
	 * 
	 * @return literal's text
	 */
	public abstract String getText();

}