package fortran.reader;

/**
 * This class represents a punched card and allows
 * to query it's properties in a convenient way.
 * 
 * @author oreissig
 */
public interface Card {

	/**
	 * @return <tt>true</tt> if a line number is associated with this card, <tt>false</tt> otherwise
	 */
	public abstract boolean hasLineNumber();

	/**
	 * @return line (card) number, undefined if none has been given
	 */
	public abstract int getLineNumber();

	/**
	 * @return <tt>true</tt> if this card contains a comment, <tt>false</tt> otherwise
	 */
	public abstract boolean isComment();

	/**
	 * @return <tt>true</tt> if there is a statement number associated with this card, <tt>false</tt> otherwise
	 */
	public abstract boolean hasNumber();

	/**
	 * @return statement number associated with this card, undefined if there is no number
	 * @see #hasNumber()
	 */
	public abstract int getNumber();

	/**
	 * @return <tt>true</tt> if this card is a continuation of the previous card, <tt>false</tt> otherwise
	 */
	public abstract boolean isContinuation();

	/**
	 * @return character used to mark the continuation, <tt>'0'</tt> if none
	 */
	public abstract char getContinuation();

	/**
	 * @return <tt>true</tt> if this card has a statement, <tt>false</tt> otherwise
	 */
	public abstract boolean hasStatement();

	/**
	 * @return text section of the statement contained in this card, <tt>null</tt> if none
	 */
	public abstract String getStatement();

	public abstract int getStatementOffset();

	/**
	 * @return indentifying information at the right end of the card, <tt>null</tt> if none
	 */
	public abstract boolean hasIdentifier();

	/**
	 * @return indentifying information at the right end of the card, <tt>null</tt> if none
	 */
	public abstract String getIdentifier();

}