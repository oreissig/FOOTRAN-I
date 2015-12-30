package com.github.oreissig.footran1.checks

import static com.github.oreissig.footran1.checks.Type.FIXED
import static com.github.oreissig.footran1.checks.Type.FLOAT

import org.antlr.v4.runtime.tree.ParseTreeWalker

import spock.lang.Unroll

import com.github.oreissig.footran1.AbstractFootranSpec
import com.github.oreissig.footran1.checks.TypeCheck.TypeCheckException

@Unroll
class TypeCheckSpec extends AbstractFootranSpec {
    
    final TypeCheck types = new TypeCheck()
    
    def 'empty program is okay'() {
        when:
        typeCheck('')
        
        then:
        noExceptionThrown()
    }
    
    def '#desc #src is of type #type'(type,desc,src) {
        when:
        typeCheck(expression(src))
        
        then:
        types.getType(expression) == type
        
        where:
        type  | desc       | src
        FLOAT | 'constant' | '1.0'
        FIXED | 'constant' | '1'
        FLOAT | 'variable' | 'A'
        FIXED | 'variable' | 'I'
    }
    
    def 'arithmetic formula type is determined by the assigned variable (#type)'(name,type) {
        when:
        typeCheck(card("$name=1"))
        
        then:
        types.getType(statement) == type
        
        where:
        name   | type
        'BARF' | FLOAT
        'INT'  | FIXED
    }
    
    def '#desc expressions must have a consistent type (#type pass)'(desc,src,type) {
        when:
        typeCheck(expression(src))
        
        then:
        types.getType(expression) == type
        
        where:
        desc             | src    | type
        'additive'       | 'A+B'  | FLOAT
        'additive'       | 'I-J'  | FIXED
        'multiplicative' | 'A*B'  | FLOAT
        'multiplicative' | 'I/J'  | FIXED
        'exponential'    | 'A**B' | FLOAT
        'exponential'    | 'I**J' | FIXED
    }
    
    def '#desc expressions must have a consistent type (fail)'(desc,src) {
        when:
        typeCheck(expression(src))
        
        then:
        thrown(TypeCheckException)
        
        where:
        desc             | src
        'additive'       | 'A+I'
        'multiplicative' | 'A*I'
        'exponential'    | 'A**I'
    }
    
    def 'sum expressions must have a consistent type (fail)'() {
        when:
        typeCheck(expression('A+I'))
        
        then:
        thrown(TypeCheckException)
    }
    
    def 'subscript expressions can be #type'(src,type) {
        when:
        typeCheck(expression(src))
        
        then:
        types.getType(expression)
        
        where:
        src      | type
        'ABC(1)' | FLOAT
        'IJK(L)' | FIXED
    }
    
    def 'subscript indices must not be FLOAT'() {
        when:
        typeCheck(expression('ABC(B)'))
        
        then:
        thrown(TypeCheckException)
    }
    
    def 'function call arguments must be of the same type (pass)'(args,type) {
        when:
        typeCheck(expression("ABSF($args)"))
        
        then:
        def someArg = unary(expression).functionCall().expression(0)
        types.getType(someArg) == type
        
        where:
        args        | type
        'A,B,5.0,Z' | FLOAT
        'I,3,N'     | FIXED
    }
    
    def 'function call arguments must be of the same type (fail)'() {
        when:
        typeCheck(expression("ABSF(A,I)"))
        
        then:
        thrown(TypeCheckException)
    }
    
    def 'function type is inferred by name (#type)'(name,type) {
        when:
        typeCheck(expression("${name}(1)"))
        
        then:
        types.getType(expression) == type
        
        where:
        name    | type
        'ABSF'  | FLOAT
        'XABSF' | FIXED
    }
    
    def 'DO loop works on fixed point indices (pass)'() {
        when:
        typeCheck(card('DO 30 I=1,N'))
        
        then:
        noExceptionThrown()
    }
    
    def 'DO loop works on fixed point indices (fail for #desc)'(desc,src) {
        when:
        typeCheck(card(src))
        
        then:
        thrown(TypeCheckException)
        
        where:
        desc                   | src
        'float index variable' | 'DO 30 A=1,M'
        'inconsistent bounds'  | 'DO 30 I=1,A'
        'float bounds'         | 'DO 30 I=A,B'
    }
    
    def '#name statement only works with fixed point variables (pass)'(name,src) {
        when:
        typeCheck(card(src))
        
        then:
        noExceptionThrown()
        
        where:
        name            | src
        'assigned goto' | 'GO TO N, (1,2,3)'
        'assign'        | 'ASSIGN 12 TO N'
        'computed goto' | 'GO TO (1,2,3), I'
    }
    
    def '#name statement only works with fixed point variables (fail)'(name,src) {
        when:
        typeCheck(card(src))
        
        then:
        thrown(TypeCheckException)
        
        where:
        name            | src
        'assigned goto' | 'GO TO A, (1,2,3)'
        'assign'        | 'ASSIGN 12 TO A'
        'computed goto' | 'GO TO (1,2,3), A'
        'read drum'     | 'READ DRUM A,2,C'
        'read drum'     | 'READ DRUM 1,B,C'
        'write drum'    | 'WRITE DRUM A,2,C'
        'write drum'    | 'WRITE DRUM 1,C,C'
    }
    
    private void typeCheck(src) {
        input = src
        ParseTreeWalker.DEFAULT.walk(types, program)
    }
}
