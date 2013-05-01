package fortran.lexer;

import static fortran.lexer.LiteralType.*;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

/**
 * This class contains the definition of the subset of {@link LiteralType}s,
 * that directly represent a keyword of the FORTRAN language:
 * <ul>
 *  <li>Control Statements:
 *  <tt>GO, TO, ASSIGN, IF,
 *  SENSE, LIGHT, SWITCH,
 *  ACCUMULATOR, OVERFLOW, QUOTIENT, DIVIDE, CHECK,
 *  PAUSE, STOP, DO, CONTINUE</tt></li>
 *  <li>Input/Output:
 *  <tt>READ, PUNCH, PRINT, INPUT,
 *  WRITE, OUTPUT,
 *  FORMAT,
 *  TAPE, DRUM,
 *  END, FILE, REWIND, BACKSPACE</tt></li>
 *  <li>Specification Statements:
 *  <tt>DIMENSTION, EQUIVALENCE, FREQUENCY</tt></li>
 * </ul> 
 * @author oreissig
 */
public class Keyword {
	/**
	 * All {@link LiteralType}s, that directly represent a
	 * keyword of the FORTRAN language.
	 */
	public static final Set<LiteralType> set;
	
	static {
		EnumSet<LiteralType> keywords = EnumSet.of(
			    // Control Statements
			    GO, TO, ASSIGN, IF,
			    SENSE, LIGHT, SWITCH,
			    ACCUMULATOR, OVERFLOW, QUOTIENT, DIVIDE, CHECK,
			    PAUSE, STOP, DO, CONTINUE,
			    // Input/Output
			    READ, PUNCH, PRINT, INPUT,
			    WRITE, OUTPUT,
			    FORMAT,
			    TAPE, DRUM,
			    END, FILE, REWIND, BACKSPACE,
			    // Specification Statements
			    DIMENSTION, EQUIVALENCE, FREQUENCY
				);
		set = Collections.unmodifiableSet(keywords);
	}
	
	/**
	 * String representations of all {@link LiteralType}s
	 * contained in {@link #set}.
	 */
	public static final Set<String> names = new AbstractSet<String>() {
		@Override
		public boolean contains(Object o) {
			try {
				LiteralType e = LiteralType.valueOf(o.toString().toUpperCase());
				return set.contains(e);
			} catch (IllegalArgumentException e) {
				return false;
			}
		}
		
		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public Iterator<String> iterator() {
			return Iterators.transform(set.iterator(), new Function<LiteralType, String>() {
				@Override
				public String apply(LiteralType input) {
					return input.name();
				}
			});
		}
		
		@Override
		public int size() {
			return set.size();
		}
	};
	
	/**
	 * Tells whether or not a given string represents a valid
	 * keyword in the FORTRAN language.
	 * 
	 * @param string to test
	 * @return true if it is a keyword, false otherwise
	 */
	public static boolean isKeyword(String string) {
		return names.contains(string);
	}
	
	private Keyword() {
		// forbid instantiation
	}
}
