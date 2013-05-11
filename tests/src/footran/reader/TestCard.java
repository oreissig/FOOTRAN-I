package footran.reader;

import footran.reader.Card;

/**
 * A {@link Card} implementation for testing purposes.
 * 
 * Use {@link #builder()} to construct a new instance.
 * 
 * @author oreissig
 */
public class TestCard implements Card, TestCardBuilder {

	private static int defaultLineNo = 1;
	private int lineNo;
	private boolean comment = false;
	private Character continuation = null;
	private Integer number = null;
	private String statement = null;
	private String ident = null;

	/**
	 * forbid direct instantiation, use {@link #builder()}
	 */
	private TestCard() {
		synchronized (TestCard.class) {
			setLineNumber(defaultLineNo++);
		}
	}

	public static TestCardBuilder builder() {
		return new TestCard();
	}

	@Override
	public Card build() {
		return this;
	}

	@Override
	public int getLineNumber() {
		return lineNo;
	}

	@Override
	public TestCardBuilder setLineNumber(int lineNo) {
		this.lineNo = lineNo;
		return this;
	}

	@Override
	public boolean isComment() {
		return comment;
	}

	@Override
	public TestCardBuilder setComment() {
		this.comment = true;
		return this;
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
	public TestCardBuilder setStatementNumber(int number) {
		this.number = number;
		return this;
	}

	@Override
	public boolean isContinuation() {
		return continuation != null;
	}

	@Override
	public char getContinuation() {
		return continuation;
	}

	@Override
	public TestCardBuilder setContinuation(char continuation) {
		this.continuation = continuation;
		return this;
	}

	@Override
	public boolean hasStatement() {
		return statement != null;
	}

	@Override
	public String getStatement() {
		return statement;
	}

	@Override
	public TestCardBuilder setStatement(String statement) {
		this.statement = statement;
		return this;
	}

	@Override
	public boolean hasIdentifier() {
		return ident != null;
	}

	@Override
	public String getIdentifier() {
		return ident;
	}

	@Override
	public TestCardBuilder setIdentifier(String ident) {
		this.ident = ident;
		return this;
	}

	@Override
	public int compareTo(Card o) {
		return this.getLineNumber() - o.getLineNumber();
	}
}
