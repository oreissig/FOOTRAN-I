package fortran.lexer;

import java.util.Iterator;

import com.google.common.collect.AbstractIterator;

import fortran.reader.Card;

public class LexerImpl extends AbstractIterator<Literal> implements Lexer {

	private final Iterator<Card> cards;

	public LexerImpl(Iterator<Card> cards) {
		this.cards = cards;
	}

	public LexerImpl(Iterable<Card> cards) {
		this.cards = cards.iterator();
	}

	@Override
	protected Literal computeNext() {
		// TODO Auto-generated method stub
		return null;
	}
}
