package com.github.oreissig.footran1.parser

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.TokenStream
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeListener
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.tree.TerminalNode

import spock.lang.Specification

@CompileStatic
abstract class AntlrSpec<P extends Parser> extends Specification {
    /**
     * @return Lexer class to instantiate
     */
    abstract Class<? extends Lexer> getLexerClass()

    /**
     * @return Parser class to instantiate
     */
    abstract Class<P> getParserClass()

    /**
     * The input to feed the parser with.
     * May be {@link InputStream}, {@link Reader} or {@link String}.
     */
    def input

    @CompileStatic(TypeCheckingMode.SKIP)
    CharStream getCharStream() {
        new ANTLRInputStream(input)
    }

    Lexer getLexer() {
        lexerClass.newInstance(charStream)
    }

    TokenStream getTokenStream() {
        new CommonTokenStream(lexer)
    }

    /**
     * @return fully initialized parser
     */
    P getParser() {
        parserClass.newInstance(tokenStream)
    }

    void checkErrorFree(ParseTree tree) {
        ParseTreeWalker.DEFAULT.walk(new NoErrorListener(), tree)
    }

    @CompileStatic
    private static class NoErrorListener implements ParseTreeListener {
        @Override
        void visitErrorNode(ErrorNode node) {
            throw new Exception(node.symbol.toString())
        }

        @Override
        void visitTerminal(TerminalNode node) {
            // do nothing
        }

        @Override
        void enterEveryRule(ParserRuleContext ctx) {
            // do nothing
        }

        @Override
        void exitEveryRule(ParserRuleContext ctx) {
            // do nothing
        }
    }
    
    List<Token> getTokens() {
        def l = lexer
        List<Token> t = []
        while (l.nextToken().type != Lexer.EOF)
            t << l.token
        return t
    }
}
