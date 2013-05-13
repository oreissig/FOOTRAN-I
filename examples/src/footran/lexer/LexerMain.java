package footran.lexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import footran.reader.Card;
import footran.reader.CardReader;
import footran.reader.ReaderMain;

public class LexerMain {
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			args = new String[3];
			args[0] = "src/examples/ex1.f";
			args[1] = "src/examples/ex2.f";
			args[2] = "src/examples/ex3.f";
		}
		
		for (String file : args) {
			System.out.println(file);
			InputStream in = new FileInputStream(file);
			CardReader cards = ReaderMain.createCardReader(in);
			Lexer lex = createLexer(cards);
			
			while (lex.hasNext())
				System.out.println(lex.next().toString());
			System.out.println();
		}
	}
	
	public static Lexer createLexer(Iterator<Card> cardReader) {
		return new LexerImpl(cardReader);
	}
}
