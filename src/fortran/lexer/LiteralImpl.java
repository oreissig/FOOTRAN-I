package fortran.lexer;

import fortran.reader.Card;

class LiteralImpl implements Literal {
	private final LiteralType type;
	private final Card card;
	private final int offset;
	private final String text;

	public LiteralImpl(LiteralType type, Card card, int offset, String text) {
		this.type = type;
		this.card = card;
		this.offset = offset;
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see fortran.lexer.Literal#getType()
	 */
	@Override
	public LiteralType getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see fortran.lexer.Literal#getCard()
	 */
	@Override
	public Card getCard() {
		return card;
	}

	/* (non-Javadoc)
	 * @see fortran.lexer.Literal#getOffset()
	 */
	@Override
	public int getOffset() {
		return offset;
	}
	
	/* (non-Javadoc)
	 * @see fortran.lexer.Literal#getText()
	 */
	@Override
	public String getText() {
		return text;
	}
}
