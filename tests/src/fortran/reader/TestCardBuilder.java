package fortran.reader;

/**
 * Chainable builder for conveniently constructing
 * Card instances for testing purposes.
 * 
 * @author oreissig
 */
public interface TestCardBuilder {

	public TestCardBuilder setLineNumber(int lineNo);

	public TestCardBuilder setComment(boolean comment);

	public TestCardBuilder setContinuation(char continuation);

	public TestCardBuilder setStatement(String statement);

	public TestCardBuilder setIdentifier(String ident);

	public TestCardBuilder setStatementNumber(int number);

	/**
	 * Finishes construction of the card.
	 * This builder must not be used after calling this method.
	 * 
	 * @return constructed Card instance
	 */
	public Card build();
}