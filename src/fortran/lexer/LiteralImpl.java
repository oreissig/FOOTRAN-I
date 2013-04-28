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

	@Override
	public LiteralType getType() {
		return type;
	}

	@Override
	public Card getCard() {
		return card;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public String getText() {
		return text;
	}
}
