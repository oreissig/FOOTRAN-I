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
	 * Adds a {@link Card}, that makes up this Statement.
	 * 
	 * @param nextCard
	 *            to be added
	 */
	public void addLiteral(Literal nextLiteral);
	
	/**
	 * Finishes construction of this Statement.
	 * 
	 * @return immutable Statement
	 */
	public Statement build();
}
