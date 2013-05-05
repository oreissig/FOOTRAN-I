package fortran.reader;

public class TestCard implements Card, TestCardBuilder {

	private int lineNo = -1;
	private boolean comment = false;
	private Character continuation = null;
	private Integer number = null;
	private String statement = null;
	private String ident = null;

	/**
	 * forbid direct instantiation, use {@link #builder()}
	 */
	private TestCard() {
	}

	public static TestCardBuilder builder() {
		return new TestCard();
	}

	public Card build() {
		return this;
	}

	@Override
	public boolean hasLineNumber() {
		return lineNo >= 0;
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
	public TestCardBuilder setComment(boolean comment) {
		this.comment = comment;
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

	public Integer getLineNo() {
		return lineNo;
	}

	@Override
	public TestCardBuilder setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
		return this;
	}

	public Integer getNumber() {
		return number;
	}

	@Override
	public TestCardBuilder setNumber(Integer number) {
		this.number = number;
		return this;
	}

	public String getIdent() {
		return ident;
	}

	@Override
	public int compareTo(Card o) {
		return this.getLineNumber() - o.getLineNumber();
	}
}
