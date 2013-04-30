package fortran.lexer;

import static fortran.lexer.LiteralType.*;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

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
	
	private Keyword() {
		// forbid instantiation
	}
}
