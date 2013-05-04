package fortran.lexer;

import java.util.List;
import java.util.SortedSet;

import fortran.reader.Card;

/**
 * A statement is defined by one or more {@link Card}s and contains a series of
 * {@link Literal}s.
 * 
 * @author oreissig
 */
public interface Statement {

	/**
	 * Tells whether or not there is a statement number associated.
	 * 
	 * @return true if a statement number has been provided, false otherwise
	 */
	public boolean hasStatementNumber();

	/**
	 * Gets this statement's statement number.
	 * 
	 * @return number of this statement
	 * @throws NullPointerException
	 *             if there is no statement number given, check
	 *             {@link #hasStatementNumber()} first to avoid
	 */
	public int getStatementNumber();

	/**
	 * Gets all {@link Card}s that make up this Statement.
	 * 
	 * @return cards that make up this Statement
	 */
	public SortedSet<Card> getCards();

	/**
	 * Adds a {@link Card}, that makes up this Statement.
	 * 
	 * @param nextCard
	 *            to be added
	 */
	public void addCard(Card nextCard);

	/**
	 * Gets all {@link Card}s that make up this Statement.
	 * 
	 * @return cards that make up this Statement
	 */
	public List<Literal> getLiterals();

	/**
	 * Adds a {@link Card}, that makes up this Statement.
	 * 
	 * @param nextCard
	 *            to be added
	 */
	public void addLiteral(Literal nextLiteral);
}
