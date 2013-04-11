package fortran.reader;

import java.io.BufferedReader;
import java.io.IOException;

public class Card {
	private static final int NO_NUMBER = -1;
	
	private final boolean comment;
	private final char continuation;
	private final int number;
	private final String ident;
	private final String statement;
	
	public Card(String line) {
		if (line.length() > 80)
			throw new IllegalArgumentException("line too long");
		if (line.contains("\n"))
			throw new IllegalArgumentException("statements can only have one line");
		
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
			statement = line.substring(6, remain);
		} else {
			statement = null;
		}
		
		if (line.length() > 72) {
			ident = line.substring(72, line.length());
		} else {
			ident = null;
		}
	}
	
	public Card(BufferedReader reader) throws IOException {
		this(reader.readLine());
	}
	
	public boolean isComment() {
		return comment;
	}
	
	public boolean hasNumber() {
		return number != NO_NUMBER;
	}
	
	public int getNumber() {
		if (hasNumber())
			return number;
		else
			throw new IllegalStateException("There is no statement number associated");
	}
	
	public boolean isContinuation() {
		return continuation != '0';
	}
	
	public char getContinuation() {
		return continuation;
	}
	
	public String getStatement() {
		return statement;
	}
	
	public String getIdentifier() {
		return ident;
	}
}
