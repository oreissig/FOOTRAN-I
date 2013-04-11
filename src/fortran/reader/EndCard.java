package fortran.reader;

public final class EndCard extends Card {

	public EndCard() {
		super("C");
	}

	public EndCard(int lineNo) {
		super("C", lineNo);
	}
}
