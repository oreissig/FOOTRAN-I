package com.github.oreissig.footran1.parser;

import com.github.oreissig.footran1.parser.AntlrModule.SpecificAntlrModule;
import com.github.oreissig.footran1.parser.FootranParser.ProgramContext;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ParserModule extends SpecificAntlrModule {

    @Override
    protected void configure() {
        bindLexer(FootranLexer.class);
        bindParser(FootranParser.class);
    }
    
    @Provides @Singleton
    protected ProgramContext getProgram(FootranParser p) {
        return p.program();
    }
}
