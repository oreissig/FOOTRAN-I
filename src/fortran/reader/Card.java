package fortran.reader;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * This class represents a punched card and parses its columns
 * to query it's properties in a convenient way.
 * 
 * @author oreissig
 */
public class Card {
	private static final int NO_NUMBER = -1;

	private final int lineNo;
	private final boolean comment;
	private final char continuation;
	private final int number;
	private final String statement;
	private final String ident;
	
	/**
	 * Creates a new punched card representation without a
	 * line number.
	 * 
	 * @param reader to read the next line from
	 * @throws IOException
	 */
	public Card(BufferedReader reader) throws IOException {
		this(reader, NO_NUMBER);
	}
	
	/**
	 * Creates a new punched card representation without a
	 * line number.
	 * 
	 * @param line to parse
	 */
	public Card(String line) {
		this(line, NO_NUMBER);
	}
	
	/**
	 * Creates a new punched card representation.
	 * 
	 * @param reader to read the next line from
	 * @param lineNo number of line (card)
	 * @throws IOException
	 */
	public Card(BufferedReader reader, int lineNo) throws IOException {
		this(reader.readLine());
	}
	
	/**
	 * Creates a new punched card representation.
	 * 
	 * @param line to parse
	 * @param lineNo number of line (card)
	 */
	public Card(String line, int lineNo) {
		if (line.length() > 80)
			throw new IllegalArgumentException("line too long");
		if (line.contains("\n"))
			throw new IllegalArgumentException("statements can only have one line");
		
		this.lineNo = lineNo;
		line = line.toUpperCase();
		
		if (line.length() >= 1 && line.charAt(0) == 'C')
			comment = true;
		else
			comment = false;

		if (line.length() >= 5) {
			int start = comment ? 1 : 0;
			String numTag = line.substring(start, 5).trim();
			if (numTag.isEmpty())
				number = NO_NUMBER;
			else
				number = Integer.parseInt(numTag);
		} else {
			number = NO_NUMBER;
		}
		
		if (line.length() >= 6) {
			char c = line.charAt(5);
			if (Character.isWhitespace(c))
				continuation = '0';
			else
				continuation = c;
		} else {
			continuation = '0';
		}
		
		if (line.length() > 6) {
			int remain;
			if (line.length() >= 72)
				remain = 72;
			else
				remain = line.length();
			statement = line.substring(6, remain).trim();
		} else {
			statement = null;
		}
		
		if (line.length() > 72) {
			ident = line.substring(72, line.length());
		} else {
			ident = null;
		}
	}
	
	/**
	 * @return <tt>true</tt> if a line number is associated with this card, <tt>false</tt> otherwise
	 */
	public boolean hasLineNumber() {
		return lineNo != NO_NUMBER;
	}
	
	/**
	 * @return line (card) number, undefined if none has been given
	 */
	public int getLineNumber() {
		return lineNo;
	}
	
	/**
	 * @return <tt>true</tt> if this card contains a comment, <tt>false</tt> otherwise
	 */
	public boolean isComment() {
		return comment;
	}
	
	/**
	 * @return <tt>true</tt> if there is a statement number associated with this card, <tt>false</tt> otherwise
	 */
	public boolean hasNumber() {
		return number != NO_NUMBER;
	}
	
	/**
	 * @return statement number associated with this card, undefined if there is no number
	 * @see #hasNumber()
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * @return <tt>true</tt> if this card is a continuation of the previous card, <tt>false</tt> otherwise
	 */
	public boolean isContinuation() {
		return continuation != '0';
	}
	
	/**
	 * @return character used to mark the continuation, <tt>'0'</tt> if none
	 */
	public char getContinuation() {
		return continuation;
	}
	
	/**
	 * @return <tt>true</tt> if this card has a statement, <tt>false</tt> otherwise
	 */
	public boolean hasStatement() {
		return statement != null && !statement.isEmpty();
	}
	
	/**
	 * @return text section of the statement contained in this card, <tt>null</tt> if none
	 */
	public String getStatement() {
		return statement;
	}
	
	/**
	 * @return indentifying information at the right end of the card, <tt>null</tt> if none
	 */
	public boolean hasIdentifier() {
		return ident != null && !ident.isEmpty();
	}
	
	/**
	 * @return indentifying information at the right end of the card, <tt>null</tt> if none
	 */
	public String getIdentifier() {
		return ident;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (hasLineNumber())
			sb.append(getLineNumber()).append(": ");
		if (isComment())
			sb.append("Comment: ");
		if (hasNumber())
			sb.append("Number ").append(getNumber()).append(": ");
		if (isContinuation())
			sb.append("Continuation ").append(getContinuation()).append(": ");
		if (hasStatement())
			sb.append("\"").append(getStatement()).append("\"");
		if (hasIdentifier())
			sb.append(" (").append(getIdentifier()).append(")");
		
		return sb.toString();
	}
}
