package com.github.oreissig.footran1.parser

import groovy.transform.CompileStatic

import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker

import com.github.oreissig.footran1.parser.FootranParser.ProgramContext

@CompileStatic
abstract class AbstractFootranSpec extends AntlrSpec<FootranParser>
{
    final Class<FootranParser> parserClass = FootranParser
    final Class<FootranLexer> lexerClass = FootranLexer

    ProgramContext getProgram() {
        parser.program()
    }

    void checkErrorFree(ParseTree tree) {
        ParseTreeWalker.DEFAULT.walk(new NoErrorListener(), tree)
    }

    @CompileStatic
    private static class NoErrorListener extends FootranBaseListener {
        @Override
        void visitErrorNode(ErrorNode node) {
            throw new Exception(node.symbol.toString())
        }
    }
}
