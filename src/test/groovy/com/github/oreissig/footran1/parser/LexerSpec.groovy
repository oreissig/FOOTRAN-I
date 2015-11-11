package com.github.oreissig.footran1.parser

import static com.github.oreissig.footran1.parser.FootranLexer.*
import spock.lang.Unroll

@Unroll
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
}
