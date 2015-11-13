package com.github.oreissig.footran1.parser

import groovy.transform.CompileStatic
import groovy.transform.Memoized

import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTreeWalker

import com.github.oreissig.footran1.parser.FootranParser.CardContext
import com.github.oreissig.footran1.parser.FootranParser.ProgramContext

@CompileStatic
abstract class AbstractFootranSpec extends AntlrSpec<FootranParser>
{
    final Class<FootranParser> parserClass = FootranParser
    final Class<FootranLexer> lexerClass = FootranLexer

    @Memoized
    ProgramContext getProgram() {
        parser.program()
    }

    List<CardContext> getCards() {
        program.card()
    }

    void noParseError() {
        ParseTreeWalker.DEFAULT.walk(new NoErrorListener(), program)
    }

    @CompileStatic
    private static class NoErrorListener extends FootranBaseListener {
        @Override
        void visitErrorNode(ErrorNode node) {
            throw new Exception(node.symbol.toString())
        }
    }
}
