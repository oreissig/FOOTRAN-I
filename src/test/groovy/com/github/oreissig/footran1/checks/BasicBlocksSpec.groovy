package com.github.oreissig.footran1.checks

import org.antlr.v4.runtime.tree.ParseTreeWalker

import spock.lang.Unroll

import com.github.oreissig.footran1.AbstractFootranSpec

@Unroll
class BasicBlocksSpec extends AbstractFootranSpec {
    
    final BasicBlocks basicBlocks = new BasicBlocks()
    
    def 'empty program only has root block'() {
        when:
        analyze('')
        
        then:
        noExceptionThrown()
        blocks.size() == 1
    }
    
    def 'basic blocks have a statement number if available'() {
        when:
        analyze('  123   A=B')
        
        then:
        // null is the (empty) root block
        blocks*.statementNumber == [null,123]
    }
    
    def 'basic blocks are chained correctly'() {
        when:
        analyze('''\
   1    A=B
   2    C=D
   3    E=F
        ''')
        
        then:
        blocks.size() == 4
        blocks*.previous == [null] + blocks[0..2]
        blocks*.next     == blocks[1..3] + [null]
    }
    
    private void analyze(src) {
        input = src
        ParseTreeWalker.DEFAULT.walk(basicBlocks, program)
    }
    
    private List<BasicBlock> getBlocks() {
        basicBlocks.blocks
    }
}
