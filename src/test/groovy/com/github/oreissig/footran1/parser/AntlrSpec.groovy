package com.github.oreissig.footran1.parser

import groovy.transform.CompileStatic
import groovy.transform.Memoized
import groovy.transform.TypeCheckingMode

import org.antlr.v4.runtime.ANTLRErrorListener
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Lexer
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.TokenStream

import spock.lang.Specification

@CompileStatic
abstract class AntlrSpec<P extends Parser> extends Specification {
    
    protected Closure onError = { String msg -> throw new RuntimeException(msg) }
    protected ANTLRErrorListener errorListener = new NoSyntaxErrorsListener()
    
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
        Lexer l = lexerClass.newInstance(charStream)
        l.addErrorListener errorListener
        return l
    }

    TokenStream getTokenStream() {
        new CommonTokenStream(lexer)
    }

    /**
     * @return fully initialized parser
     */
    P getParser() {
        Parser p = parserClass.newInstance(tokenStream)
        p.addErrorListener errorListener
        return p
    }

    @Memoized
    List<Token> getTokens() {
        def l = lexer
        List<Token> t = []
        while (l.nextToken().type != Lexer.EOF)
            t << l.token
        return t
    }
    
    @CompileStatic
    private class NoSyntaxErrorsListener extends BaseErrorListener {
        @Override
        void syntaxError(Recognizer recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
            onError.call(msg)
        }
    }
    
    void onSyntaxError(Closure action) {
        onError = action
    }
}
