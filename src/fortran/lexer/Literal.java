package fortran.lexer;

import fortran.reader.Card;

public interface Literal {

	public abstract LiteralType getType();

	public abstract Card getCard();

	public abstract int getOffset();

	public abstract String getText();

}