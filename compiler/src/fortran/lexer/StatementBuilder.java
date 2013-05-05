package fortran.lexer;

import fortran.reader.Card;

/**
 * Interface used to construct a FORTRAN statement.
 * 
 * @author oreissig
 */
interface StatementBuilder {

	/**
	 * Adds a {@link Card}, that makes up this Statement.
	 * 
	 * @param nextCard
	 *            to be added
	 */
	public void addCard(Card nextCard);

	/**
	 * Adds a {@link Token} to this Statement.
	 * 
	 * @param nextToken
	 *            to be added
	 */
	public void addToken(Token nextToken);
	
	/**
	 * Finishes construction of this Statement.
	 * 
	 * @return immutable Statement
	 */
	public Statement build();
}
