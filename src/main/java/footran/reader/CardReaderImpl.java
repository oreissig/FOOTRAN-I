package footran.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.AbstractIterator;

class CardReaderImpl extends AbstractIterator<Card> implements CardReader {

	private static final Logger log = LoggerFactory.getLogger(CardReader.class);

	private final BufferedReader reader;
	private int lineNo = 1;

	public CardReaderImpl(String source) {
		this(new StringReader(source));
	}

	public CardReaderImpl(InputStream input) {
		this(new InputStreamReader(input));
	}

	public CardReaderImpl(Reader reader) {
		if (reader instanceof BufferedReader)
			this.reader = (BufferedReader) reader;
		else
			this.reader = new BufferedReader(reader);
	}

	@Override
	protected Card computeNext() {
		try {
			String line = reader.readLine();
			if (line != null) {
				log.debug("read card {}", lineNo);
				return new CardImpl(line, lineNo++);
			} else {
				log.debug("end of cards");
				return endOfData();
			}
		} catch (IOException e) {
			throw new CardException("could not read next card", e);
		}
	}
}
