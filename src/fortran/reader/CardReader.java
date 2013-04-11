package fortran.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * The CardReader drives the initial read of a source
 * file and the basic parsing of the punched card format.
 * 
 * @author oreissig
 */
public class CardReader {
	/**
	 * Reads the given String to create an according
	 * List of Cards.
	 * 
	 * @param source source code
	 * @return cards
	 * @throws IOException
	 */
	public List<Card> read(String source) throws IOException {
		return read(new StringReader(source));
	}
	
	/**
	 * Reads from the given InputStream to create an according
	 * List of Cards.
	 * 
	 * @param input to read source code from
	 * @return cards
	 * @throws IOException
	 */
	public List<Card> read(InputStream input) throws IOException {
		try {
			return read(new InputStreamReader(input, "US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			throw new IOException("ASCII not supported o_O", e);
		}
	}
	
	/**
	 * Reads from the given Reader to create an according
	 * List of Cards.
	 * 
	 * @param reader to read source code from
	 * @return cards
	 * @throws IOException
	 */
	public List<Card> read(Reader reader) throws IOException {
		return read(new BufferedReader(reader));
	}

	/**
	 * Reads from the given Reader to create an according
	 * List of Cards.
	 * 
	 * @param reader to read source code from
	 * @return cards
	 * @throws IOException
	 */
	public List<Card> read(BufferedReader reader) throws IOException {
		List<Card> listing = new ArrayList<>();
		String line;
		int lineNo = 1;
		while ((line = reader.readLine()) != null) {
			Card c = new Card(line, lineNo++);
			listing.add(c);
		}
		listing.add(new EndCard(lineNo));
		return listing;
	}
}
