package fortran.parser;

import com.google.inject.AbstractModule;

public class ParserModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Parser.class).to(ParserImpl.class);
	}
}
