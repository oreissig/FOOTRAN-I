package fortran.lexer;

import com.google.common.collect.PeekingIterator;

import fortran.reader.Card;

/**
 * The Lexer creates a stream of {@link Token}s
 * from a stream of {@link Card}s.
 * 
 * @author oreissig
 */
public interface Lexer extends PeekingIterator<Statement> {
}
