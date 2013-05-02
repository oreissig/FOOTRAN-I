package fortran.lexer;

import java.util.Iterator;

import fortran.reader.Card;

/**
 * The Lexer creates a stream of {@link Literal}s
 * from a stream of {@link Card}s.
 * 
 * @author oreissig
 */
public interface Lexer extends Iterator<Statement> {
}
