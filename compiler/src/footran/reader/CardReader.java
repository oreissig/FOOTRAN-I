package footran.reader;

import com.google.common.collect.PeekingIterator;

/**
 * The CardReader drives the initial read of a source
 * file and the basic parsing of the punched card format.
 * 
 * @author oreissig
 */
public interface CardReader extends PeekingIterator<Card> {
}