package com.github.oreissig.footran1.parser

import spock.lang.Stepwise
import spock.lang.Unroll

import com.github.oreissig.footran1.parser.FootranParser.CardContext
import com.github.oreissig.footran1.parser.FootranParser.ExpressionContext

@Unroll
@Stepwise
public class ParserSpec extends AbstractFootranSpec {
    
    def 'empty program parses successfully'() {
        given:
        input = ''
        
        expect:
        program.card().empty
    }
    
    def 'card "#src" is recognized'(src, num, body) {
        given:
        input = src
        
        expect:
        noParseError()
        cards.size() == 1
        cards[0].STMTNUM()?.text == num
        cards[0].statement().text == body
        
        where:
        src           | num  | body
        '      A=B'   | null | 'A=B'
        '      C=D\n' | null | 'C=D'
        '   23 X=Y'   | '23' | 'X=Y'
    }
    
    def 'multiple cards are parsed correctly'() {
        given:
        input = '''\
      A=B
C     FOO
      C=D'''
        
        expect:
        noParseError()
        cards.size() == 2
        cards[0].statement().text == 'A=B'
        cards[1].statement().text == 'C=D'
    }
    
    def 'arithmetic formulas are parsed correctly (#var=#expr)'(var, expr) {
        given:
        input = card("$var=$expr")
        
        expect:
        noParseError()
        def af = cards[0].statement().arithmeticFormula()
        af != null
        def lhs = [af.VAR_ID(), af.FUNC_CANDIDATE(), af.subscript()].find()
        lhs.text == var
        af.expression().text == expr
        
        where:
        var    | expr
        'A'    | 'B'
        'FO0'  | '4711'
        'A(1)' | '42'   // subscript variable
        'FOOF' | 'BARF' // function candidate that is a non-subscripted var
    }
    
    def 'subscript variables are parsed correctly (#var)'(var, dimensions, name, v, c) {
        given:
        input = card("$var=1")
        
        expect:
        noParseError()
        def s = cards[0].statement().arithmeticFormula().subscript()
        s.VAR_ID().text == name
        s.subscriptExpression().size() == dimensions
        def exp = s.subscriptExpression()[0]
        exp.VAR_ID()?.text == v
        exp.intConst()?.text == c
        
        where:
        var         | dimensions | name  | v    | c
        'A(I)'      | 1          | 'A'   | 'I'  | null
        'BLA(3)'    | 1          | 'BLA' | null | '3'
        // TODO test failure for more than 3 dimensions
        'C(1,B,C3)' | 3          | 'C'   | null | '1'
    }
    
    // TODO incomplete
    def 'expressions can be parsed (#type)'(type, src) {
        expect:
        def exp = parseExpression(src)
        exp.text == src
        exp."$type"()
        
        where:
        type       | src
        'VAR_ID'   | 'ABC'
        'call'     | 'ABCF(1,A)'
        'intConst' | '1'
        'fpConst'  | '1.0'
    }
    
    ExpressionContext parseExpression(String src) {
        input = card("A=$src")
        program.card(0).statement().arithmeticFormula().expression()
    }
}
