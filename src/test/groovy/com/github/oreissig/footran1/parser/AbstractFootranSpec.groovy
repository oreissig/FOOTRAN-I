package com.github.oreissig.footran1.parser

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.Memoized

import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTreeWalker

import com.github.oreissig.footran1.parser.FootranParser.CardContext
import com.github.oreissig.footran1.parser.FootranParser.ExpressionContext
import com.github.oreissig.footran1.parser.FootranParser.ProgramContext
import com.github.oreissig.footran1.parser.FootranParser.StatementContext
import com.github.oreissig.footran1.parser.FootranParser.UnaryExpressionContext

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

    // Spock does strange stuff when using CompileStatic
    @CompileDynamic
    StatementContext getStatement() {
        assert cards*.text.size() == 1
        cards[0].statement()
    }

    void noParseError() {
        ParseTreeWalker.DEFAULT.walk(new NoErrorListener(), program)
    }

    @CompileStatic
    private static class NoErrorListener extends FootranBaseListener {
        @Override
        void visitErrorNode(ErrorNode node) {
            throw new ParseError(node)
        }
    }
    
    @CompileStatic
    static class ParseError extends RuntimeException {
        final ErrorNode node
        
        ParseError(ErrorNode node) {
            super(node.symbol.toString())
            this.node = node
        }
    }
    
    String card(String body) {
        assert !('\n' in body)
        return "      $body"
    }
    
    ExpressionContext parseExpression(String src) {
        input = card("A=$src")
        statement.arithmeticFormula().expression()
    }
    
    // workaround for strange compile error
    @CompileDynamic
    UnaryExpressionContext unary(ExpressionContext expr) {
        def sum = expr.sum()
        assert !sum.sign()
        def prod = sum.product()
        assert !prod.mulOp()
        def pow = prod.power()
        assert !pow.POWER()
        return pow.unaryExpression().first()
    }
}
