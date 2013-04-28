package fortran.lexer;

import java.util.List;
import java.util.SortedSet;

import com.google.common.collect.PeekingIterator;

import fortran.reader.Card;

/**
 * A statement is defined by one or more {@link Card}s and contains a series of
 * {@link Literal}s.
 * 
 * @author oreissig
 */
public interface Statement {

	/**
	 * Gets all {@link Card}s that make up this Statement.
	 * 
	 * @return cards that make up this Statement
	 */
	public SortedSet<Card> getCards();
	
	/**
	 * Adds a {@link Card}, that makes up this Statement.
	 * 
	 * @param nextCard to be added
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
	 * @param nextCard to be added
	 */
	public void addLiteral(Literal nextLiteral);
}
