package fortran.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

class CardReaderImpl implements CardReader {

	@Override
	public PeekingIterator<Card> read(final String source) {
		Reader r = new StringReader(source);
		try {
			return read(r);
		} catch (IOException e) {
			// this will not happen, StringReader doesn't throw IOException
			// return empty iterator nevertheless
			Iterator<Card> empty = Iterators.emptyIterator();
			return Iterators.peekingIterator(empty);
		}
	}
	
	@Override
	public PeekingIterator<Card> read(InputStream input) throws IOException {
		try {
			return read(new InputStreamReader(input, "US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new IOException("ASCII not supported o_O", e);
		}
	}
	
	@Override
	public PeekingIterator<Card> read(Reader reader) throws IOException {
		return read(new BufferedReader(reader));
	}

	@Override
	public PeekingIterator<Card> read(final BufferedReader reader) throws IOException {
		return new CardIterator(reader);
	}
	
	protected class CardIterator extends AbstractIterator<Card> implements PeekingIterator<Card> {
		private final BufferedReader reader;
		private int lineNo = 1;
		
		public CardIterator(BufferedReader reader) {
			this.reader = reader;
		}
		
		@Override
		protected Card computeNext() {
			try {
				String line = reader.readLine();
				return new CardImpl(line, lineNo++);
			} catch (IOException e) {
				return endOfData();
			}
		}
	}
}
