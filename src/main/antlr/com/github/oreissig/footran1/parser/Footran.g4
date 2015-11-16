/* 
 * A grammar for the original version of FORTRAN as described
 * by IBM's programmer's reference manual from 1956:
 * http://www.fortran.com/FortranForTheIBM704.pdf
 * 
 * This grammar tries to keep its wording close to the original
 * language reference, e.g. an array is called subscripted
 * variable.
 */
grammar Footran;

@header {
package com.github.oreissig.footran1.parser;
}

@lexer::members {
/**
 * FORTRAN-I encapsulates type information in its identifiers, but
 * unfortunately there are cases that require context to be certain:
 * IDs with 4 or more characters ending with 'F' may either denote a
 * non-subscripted variable or a function.
 * 
 * @param text the ID to analyze
 * @return true if we know for sure, that text is a variable ID
 */
private boolean isVariable(String text) {
    return text.length() < 4 || !text.endsWith("F");
}
}


// parser rules

program : card*;
card : STMTNUM? statement NEWCARD?;
statement : arithmeticFormula
          // GO TO family
          | uncondGoto
          | assignedGoto
          | assign
          | computedGoto
          // TODO more to come
          ;

arithmeticFormula : (VAR_ID | FUNC_CANDIDATE | subscript) '=' expression;

uncondGoto   : 'GO' 'TO' ufixedConst;
assignedGoto : 'GO' 'TO' variable ',' '(' ufixedConst (',' ufixedConst)* ')';
assign       : 'ASSIGN' ufixedConst 'TO' variable;
computedGoto : 'GO' 'TO' '(' ufixedConst (',' ufixedConst)* ')' ',' variable;

variable   : VAR_ID | FUNC_CANDIDATE;
subscript  : var=VAR_ID '(' subscriptExpression (','
                            subscriptExpression (','
                            subscriptExpression )? )? ')';
subscriptExpression : constant=ufixedConst
                    | (factor=ufixedConst '*')? index=variable (sign summand=ufixedConst)?;

expression : sign? unsigned=unsignedExpression;
unsignedExpression : '(' expression ')' | variable | functionCall | ufixedConst | ufloatConst;

functionCall : function=FUNC_CANDIDATE '(' expression (',' expression)* ')';

ufixedConst : NUMBER ;
fixedConst  : sign? unsigned=ufixedConst ;
ufloatConst : integer=NUMBER? '.' (fraction=NUMBER | fractionE=FLOAT_FRAC exponent=fixedConst)? ;
floatConst  : sign? unsigned=ufloatConst ;

sign  : (PLUS|MINUS);
mulOp : (MUL|DIV);


// lexer rules

// prefix area processing
COMMENT  : {getCharPositionInLine() == 0}? 'C' ~('\n')* '\n'? -> skip ;
STMTNUM  : {getCharPositionInLine() < 5}? [1-9][0-9]* ;
CONTINUE : NEWCARD . . . . . ~[' '|'0'] -> skip ;

// body processing
fragment DIGIT  : {getCharPositionInLine() > 5}? [0-9];
fragment LETTER : {getCharPositionInLine() > 5}? [A-Z];
fragment ALFNUM : {getCharPositionInLine() > 5}? [A-Z0-9];

NUMBER  : DIGIT+ ;
// use a separate token to avoid recognizing the float exponential E as ID
FLOAT_FRAC : NUMBER 'E';

VAR_ID         : LETTER ALFNUM* {isVariable(getText())}? ;
// function candidate refers to either a function or a non-subscripted variable
FUNC_CANDIDATE : LETTER ALFNUM+ {!isVariable(getText())}? ;

PLUS  : '+';
MINUS : '-';
MUL   : '*';
DIV   : '/';
POWER : '**';

// return newlines to parser
NEWCARD : '\r'? '\n' ;
// skip spaces and tabs
WS      : ' '+ -> skip ;
