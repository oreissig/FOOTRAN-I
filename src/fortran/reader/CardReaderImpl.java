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

public class CardReaderImpl implements CardReader {

	@Override
	public Iterable<Card> read(final String source) throws IOException {
		return new Iterable<Card>() {
			@Override
			public Iterator<Card> iterator() {
				Reader r = new StringReader(source);
				try {
					return read(r).iterator();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}
	
	@Override
	public Iterable<Card> read(InputStream input) throws IOException {
		try {
			return read(new InputStreamReader(input, "US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new IOException("ASCII not supported o_O", e);
		}
	}
	
	@Override
	public Iterable<Card> read(Reader reader) throws IOException {
		return read(new BufferedReader(reader));
	}

	@Override
	public Iterable<Card> read(final BufferedReader reader) throws IOException {
		final Iterator<Card> i = new AbstractIterator<Card>() {
			private int lineNo = 1;
			
			@Override
			protected Card computeNext() {
				try {
					String line = reader.readLine();
					return new CardImpl(line, lineNo++);
				} catch (IOException e) {
					return endOfData();
				}
			}
		};
		
		return new Iterable<Card>() {
			@Override
			public Iterator<Card> iterator() {
				return i;
			}
		};
	}
}
