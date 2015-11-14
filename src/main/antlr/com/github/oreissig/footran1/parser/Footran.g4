grammar Footran;

@header {
package com.github.oreissig.footran1.parser;
}

// parser rules

program : card*;
card : STMTNUM? statement NEWCARD?;
statement : arithmeticFormula
          // TODO more to come
          ;

arithmeticFormula : ID '=' expression;

expression : ID | call | intConst | fpConst;

// treat subscripted variables as function calls
call : ID '(' expression (',' expression)* ')';

intConst : ('+'|'-')? NUMBER ;
fpConst  : ('+'|'-')? NUMBER? '.' NUMBER? ('E' intConst)? ;


// lexer rules

// prefix area processing
COMMENT : {getCharPositionInLine() == 0}? 'C' ~('\n')* '\n'? -> skip ;
STMTNUM : {getCharPositionInLine() < 5}? [0-9]+ ;
CONTINUE : NEWCARD . . . . . ~[' '|'0'] -> skip ;

// body processing
fragment DIGIT  : {getCharPositionInLine() > 5}? [0-9];
fragment LETTER : {getCharPositionInLine() > 5}? [A-Z];
fragment ALFNUM : {getCharPositionInLine() > 5}? [A-Z0-9];

ID      : LETTER ALFNUM* ;
NUMBER  : DIGIT+ ;

// return newlines to parser
NEWCARD : '\r'? '\n' ;
// skip spaces and tabs
WS      : ' '+ -> skip ;
