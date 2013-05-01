package fortran.lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import fortran.reader.Card;

class StatementImpl implements Statement {

	private final SortedSet<Card> cards = new TreeSet<>();
	private final List<Literal> literals = new ArrayList<>();

	@Override
	public SortedSet<Card> getCards() {
		return Collections.unmodifiableSortedSet(cards);
	}

	@Override
	public void addCard(Card nextCard) {
		cards.add(nextCard);
	}

	@Override
	public List<Literal> getLiterals() {
		return Collections.unmodifiableList(literals);
	}

	@Override
	public void addLiteral(Literal nextLiteral) {
		literals.add(nextLiteral);
		cards.add(nextLiteral.getCard());
	}
}
