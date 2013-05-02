package fortran;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import fortran.reader.Card;
import fortran.reader.CardReader;
import fortran.reader.CardReaderImpl;

public class ReaderMain {
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			args = new String[3];
			args[0] = "src/examples/ex1.f";
			args[1] = "src/examples/ex2.f";
			args[2] = "src/examples/ex3.f";
		}
		
		CardReader cr = new CardReaderImpl();
		for (String file : args) {
			System.out.println(file);
			InputStream in = new FileInputStream(file);
			Iterator<Card> cards = cr.read(in);
			while (cards.hasNext())
				System.out.println(cards.next().toString());
			System.out.println();
		}
	}
}
