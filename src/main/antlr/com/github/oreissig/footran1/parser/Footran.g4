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
          // TODO more to come
          ;

arithmeticFormula : (VAR_ID | FUNC_CANDIDATE | subscript) '=' expression;

subscript  : VAR_ID '(' subscriptExpression (',' subscriptExpression (',' subscriptExpression)?)? ')';
subscriptExpression : VAR_ID | uintConst | subscriptSum;
subscriptSum  : subscriptMult (sign uintConst)?;
subscriptMult : (uintConst '*')? VAR_ID;

expression : VAR_ID | call | intConst | fpConst;

// treat subscripted variables as function calls
call : FUNC_CANDIDATE '(' expression (',' expression)* ')';

uintConst : NUMBER ;
intConst  : sign? uintConst ;
ufpConst  : NUMBER? '.' NUMBER? ('E' intConst)? ;
fpConst   : sign? ufpConst ;

sign : (PLUS|MINUS);


// lexer rules

// prefix area processing
COMMENT : {getCharPositionInLine() == 0}? 'C' ~('\n')* '\n'? -> skip ;
STMTNUM : {getCharPositionInLine() < 5}? [0-9]+ ;
CONTINUE : NEWCARD . . . . . ~[' '|'0'] -> skip ;

// body processing
fragment DIGIT  : {getCharPositionInLine() > 5}? [0-9];
fragment LETTER : {getCharPositionInLine() > 5}? [A-Z];
fragment ALFNUM : {getCharPositionInLine() > 5}? [A-Z0-9];

NUMBER  : DIGIT+ ;

VAR_ID         : LETTER ALFNUM* {isVariable(getText())}? ;
// function candidate refers to either a function or a non-subscripted variable
FUNC_CANDIDATE : LETTER ALFNUM+ {!isVariable(getText())}? ;

PLUS  : '+';
MINUS : '-';

// return newlines to parser
NEWCARD : '\r'? '\n' ;
// skip spaces and tabs
WS      : ' '+ -> skip ;
