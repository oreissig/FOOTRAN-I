package com.github.oreissig.footran1.parser

import spock.lang.Stepwise
import spock.lang.Unroll

import com.github.oreissig.footran1.parser.FootranParser.CardContext
import com.github.oreissig.footran1.parser.FootranParser.ExpressionContext

@Unroll
@Stepwise
class ParserSpec extends AbstractFootranSpec {
    
    // fail on stderr
    def setupSpec() {
        System.err = Mock(PrintStream) {
            println(_) >> { throw new Exception(it.toString()) }
        }
    }
    
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
        'FOOF' | 'BAR' // function candidate that is a non-subscripted var
    }
    
    def 'pixed point constants are parsed correctly (#src)'(src, sign, mag) {
        when:
        def i = parseExpression(src).intConst()
        
        then:
        i.sign()?.text == sign
        i.unsigned.NUMBER().text == mag.toString()
        
        where:
        src      | sign | mag
        '3'      | null | 3
        '+1'     | '+'  | 1
        '-28987' | '-'  | 28987
        '0'      | null | 0
        '32767'  | null | 32767
        '-32767' | '-'  | 32767
    }
    
    def 'floating point constants are parsed correctly (#src)'(src, sign, integ, frac, expSign, expMag) {
        when:
        def f = parseExpression(src).fpConst()
        
        then:
        f.sign()?.text == sign
        def uf = f.unsigned
        uf.integer?.text == integ?.toString()
        def fraction = [uf.fraction1, uf.fraction2].find()
        fraction?.text == frac?.toString()
        def exp = uf.exponent
        exp?.sign()?.text == expSign
        exp?.uintConst()?.NUMBER()?.text == expMag?.toString()
        
        where:
        src      | sign | integ | frac   | expSign | expMag
        '17.'    | null | 17    | null   | null    | null
        '+5.0'   | '+'  | 5     | 0      | null    | null
        '-.0003' | '-'  | null  | '0003' | null    | null
        // to be able to lex the E properly, it is part of the fraction token
        '5.0E3'  | null | 5     | '0E'   | null    | 3
        '5.0E+3' | null | 5     | '0E'   | '+'     | 3
        '5.0E-7' | null | 5     | '0E'   | '-'     | 7
        '0.0'    | null | 0     | 0      | null    | null
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
