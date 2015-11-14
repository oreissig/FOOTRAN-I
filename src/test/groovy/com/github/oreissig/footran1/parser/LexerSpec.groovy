package com.github.oreissig.footran1.parser

import static com.github.oreissig.footran1.parser.FootranLexer.*
import spock.lang.Stepwise
import spock.lang.Unroll

@Unroll
@Stepwise
class LexerSpec extends AbstractFootranSpec {
    
    def 'empty program lexes successfully'() {
        when:
        input = ''
        
        then:
        tokens.empty
    }
    
    def 'comment lines are ignored'() {
        when:
        input = '''\
C     FOO
C1234XBARBAZ
'''
        
        then:
        tokens.empty
    }
    
    def 'statement numbers are recognized'(src) {
        when:
        input = src
        
        then:
        tokens*.type == [STMTNUM]
        
        where:
        src << ['    1 ',
                ' 1337 ',
                '32767 ',]
    }
    
    def 'numbers are recognized (#num)'(num) {
        when:
        input = card(num)
        
        then:
        tokens.size() == 1
        def t = tokens.first()
        t.type == NUMBER
        t.text == num
        
        where:
        num << ['1', '23', '32767']
    }
    
    def 'variable identifiers are recognized (#id)'(id) {
        when:
        input = card(id)
        
        then:
        tokens.size() == 1
        def t = tokens.first()
        t.type == VAR_ID
        t.text == id
        
        where:
        id << ['I', 'B7', 'JOBNO', 'DELTA', 'WOF']
    }
    
    def 'function identifier candidates are recognized (#id)'(id) {
        when:
        input = card(id)
        
        then:
        tokens.size() == 1
        def t = tokens.first()
        t.type == FUNC_CANDIDATE
        t.text == id
        
        where:
        id << ['SINF', 'XSINF', 'SIN0F']
    }
    
    def 'continuations work'() {
        when:
        input = '''\
      123
     XABC'''
        
        then:
        tokens*.type == [NUMBER, VAR_ID]
        tokens*.text == ['123', 'ABC']
    }
}
