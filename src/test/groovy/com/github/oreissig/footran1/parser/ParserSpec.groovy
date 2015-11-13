package com.github.oreissig.footran1.parser

import com.github.oreissig.footran1.parser.FootranParser.CardContext;

import spock.lang.Stepwise
import spock.lang.Unroll

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
        '      ABC'   | null | 'ABC'
        '      DEF\n' | null | 'DEF'
        '   23 XYZ'   | '23' | 'XYZ'
    }
    
    def 'multiple cards are parsed correctly'() {
        given:
        input = '''\
      ABC
C     FOO
      DEF'''
        
        expect:
        noParseError()
        cards.size() == 2
        cards[0].statement().text == 'ABC'
        cards[1].statement().text == 'DEF'
    }
}
