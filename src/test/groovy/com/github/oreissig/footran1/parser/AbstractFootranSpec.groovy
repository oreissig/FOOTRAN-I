package com.github.oreissig.footran1.parser

import groovy.transform.CompileStatic

@CompileStatic
abstract class AbstractFootranSpec extends AntlrSpec<FootranParser>
{
    final Class<FootranParser> parserClass = FootranParser
    final Class<FootranLexer> lexerClass = FootranLexer

    /*ProgramContext parse() {
     parser.program()
     }*/
}
