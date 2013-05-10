package footran.lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import footran.reader.Card;

class StatementImpl implements Statement, StatementBuilder {

	private final SortedSet<Card> cards = new TreeSet<>();
	private final List<Token> tokens = new ArrayList<>();
	private Integer number = null;

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
	public StatementBuilder addCard(Card nextCard) {
		boolean newly = cards.add(nextCard);
		if (newly && nextCard.hasStatementNumber()) {
			int newNumber = nextCard.getStatementNumber();
			if (number == null)
				number = newNumber;
			else
				throw new IllegalStateException(
						"Card " + nextCard.getLineNumber() +
						" defines a statement number, but " + number +
						" is already one defined by a previous card of this statement");
		}
		return this;
	}

	@Override
	public List<Token> getTokens() {
		return Collections.unmodifiableList(tokens);
	}

	@Override
	public StatementBuilder addToken(Token nextToken) {
		tokens.add(nextToken);
		return this;
	}

	@Override
	public Statement build() {
		if (cards.isEmpty())
			throw new IllegalStateException("A statement must at least contain one Card");
		else
			return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Statement ");
		if (hasStatementNumber())
			sb.append(getStatementNumber());
		sb.append(tokens.toString());
		return sb.toString();
	}
}
