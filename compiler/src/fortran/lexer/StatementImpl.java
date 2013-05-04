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
	private Integer number = null;

	/**
	 * Construct a new Statement.
	 * 
	 * @param firstCard
	 *            at least one Card is needed for a Statement
	 */
	public StatementImpl(Card firstCard) {
		addCard(firstCard);
	}

	@Override
	public boolean hasStatementNumber() {
		return number != null;
	}

	@Override
	public int getStatementNumber() {
		return number;
	}

	@Override
	public SortedSet<Card> getCards() {
		return Collections.unmodifiableSortedSet(cards);
	}

	@Override
	public void addCard(Card nextCard) {
		cards.add(nextCard);
		if (nextCard.hasStatementNumber()) {
			int newNumber = nextCard.getStatementNumber();
			if (number == null)
				number = newNumber;
			else
				throw new IllegalStateException(
						"Card " + nextCard.getLineNumber() +
						" defines a statement number, but " + number +
						" is already one defined by a previous card of this statement");
		}
	}

	@Override
	public List<Literal> getLiterals() {
		return Collections.unmodifiableList(literals);
	}

	@Override
	public void addLiteral(Literal nextLiteral) {
		literals.add(nextLiteral);
	}
}
