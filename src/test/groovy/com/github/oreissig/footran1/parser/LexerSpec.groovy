package com.github.oreissig.footran1.parser

import static com.github.oreissig.footran1.parser.FootranLexer.*
import spock.lang.Stepwise
import spock.lang.Unroll

@Unroll
@Stepwise
public class LexerSpec extends AbstractFootranSpec {
    
    def 'empty program lexes successfully'() {
        given:
        input = ''
        
        expect:
        tokens.empty
    }
    
    def 'comment lines are ignored'() {
        given:
        input = '''\
C     FOO
C1234XBARBAZ
'''
        
        expect:
        tokens.empty
    }
    
    def 'statement numbers are recognized'(src) {
        given:
        input = src
        
        expect:
        tokens*.type == [STMTNUM]
        
        where:
        src << ['    1 ',
                ' 1337 ',
                '32767 ',]
    }
    
    def 'numbers are recognized (#num)'(num) {
        given:
        input = card(num)
        
        expect:
        tokens.size() == 1
        def t = tokens.first()
        t.type == NUMBER
        t.text == num
        
        where:
        num << ['1', '23', '32767']
    }
    
    def 'identifiers are recognized (#id)'(id) {
        given:
        input = card(id)
        
        expect:
        tokens.size() == 1
        def t = tokens.first()
        t.type == ID
        t.text == id
        
        where:
        id << ['A', 'I', 'B7', 'SINF', 'XTANF']
    }
    
    def 'continuations work'() {
        given:
        input = '''\
      123
     XABC'''
        
        expect:
        tokens*.type == [NUMBER, ID]
        tokens*.text == ['123', 'ABC']
    }
}
