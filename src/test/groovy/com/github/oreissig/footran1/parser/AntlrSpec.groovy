package com.github.oreissig.footran1.parser

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.TokenStream

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

    List<Token> getTokens() {
        def l = lexer
        List<Token> t = []
        while (l.nextToken().type != Lexer.EOF)
            t << l.token
        return t
    }
}
