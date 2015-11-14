package com.github.oreissig.footran1.parser

import spock.lang.Stepwise
import spock.lang.Unroll

import com.github.oreissig.footran1.parser.FootranParser.CardContext
import com.github.oreissig.footran1.parser.FootranParser.ExpressionContext

@Unroll
@Stepwise
public class ParserSpec extends AbstractFootranSpec {
    
    def 'empty program parses successfully'() {
        when:
        input = ''
        
        then:
        program.card().empty
    }
    
    def 'card "#src" is recognized'(src, num, body) {
        when:
        input = src
        
        then:
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
        when:
        input = '''\
      A=B
C     FOO
      C=D'''
        
        then:
        noParseError()
        cards.size() == 2
        cards[0].statement().text == 'A=B'
        cards[1].statement().text == 'C=D'
    }
    
    def 'arithmetic formulas are parsed correctly (#var=#expr)'(var, expr) {
        when:
        input = card("$var=$expr")
        
        then:
        noParseError()
        def af = statement.arithmeticFormula()
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
        when:
        input = card("$var=1")
        
        then:
        noParseError()
        def s = statement.arithmeticFormula().subscript()
        s.VAR_ID().text == name
        s.subscriptExpression().size() == dimensions
        def exp = s.subscriptExpression()[0]
        exp.VAR_ID()?.text == v
        exp.uintConst()?.text == c
        
        where:
        var         | dimensions | name  | v    | c
        'A(I)'      | 1          | 'A'   | 'I'  | null
        'BLA(3)'    | 1          | 'BLA' | null | '3'
        'C(1,B)'    | 2          | 'C'   | null | '1'
        'D(1,B,C3)' | 3          | 'D'   | null | '1'
    }
    
    // subscript expressions = c1 * v +/- c2
    def 'subscript expressions are parsed correctly (#expr)'(expr, c1, v, sign, c2) {
        when:
        input = card("A($expr)=1")
        
        then:
        noParseError()
        def e = statement.arithmeticFormula().subscript().subscriptExpression()[0]
        def sum = e.subscriptSum()
        def mul = sum.subscriptMult()
        mul.uintConst()?.text == c1
        mul.VAR_ID()?.text == v
        sum.sign()?.text == sign
        sum.uintConst()?.text == c2 
        
        where:
        expr     | c1   | v    | sign | c2
        'MU+2'   | null | 'MU' | '+'  | '2'
        'MU-2'   | null | 'MU' | '-'  | '2'
        '5*J'    | '5'  | 'J'  | null | null
        '5*J+2'  | '5'  | 'J'  | '+'  | '2'
        '5*J-2'  | '5'  | 'J'  | '-'  | '2'
    }
    
    // TODO incomplete
    def 'expressions can be parsed (#type)'(type, src) {
        when:
        def exp = parseExpression(src)
        
        then:
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
        statement.arithmeticFormula().expression()
    }
}
