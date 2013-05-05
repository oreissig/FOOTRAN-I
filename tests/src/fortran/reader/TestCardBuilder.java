package fortran.reader;

public interface TestCardBuilder {

	public TestCardBuilder setLineNumber(int lineNo);

	public TestCardBuilder setComment(boolean comment);

	public TestCardBuilder setContinuation(char continuation);

	public TestCardBuilder setStatement(String statement);

	public TestCardBuilder setIdentifier(String ident);

	public TestCardBuilder setLineNo(Integer lineNo);

	public TestCardBuilder setNumber(Integer number);

	public Card build();
}