package com.github.oreissig.footran1.parser

import spock.lang.Stepwise
import spock.lang.Unroll

import com.github.oreissig.footran1.parser.FootranParser.CardContext

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
    
    def 'arithmetic formulas are parsed correctly (#src)'(src) {
        given:
        input = card(src)
        
        expect:
        noParseError()
        def s = cards[0].statement()
        s.text == src
        
        where:
        src << ['A=B']
    }
    
    String card(String body) {
        assert !body.contains('\n')
        return "      $body"
    }
}
