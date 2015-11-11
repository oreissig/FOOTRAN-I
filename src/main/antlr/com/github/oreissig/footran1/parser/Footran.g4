grammar Footran;

@header {
package com.github.oreissig.footran1.parser;
}

// parser rules

program : statement*;

statement : expression NEWCARD?;

// TODO
expression : ID;

intConst : ('+'|'-')? NUMBER ;
fpConst  : ('+'|'-')? NUMBER? '.' NUMBER? ('E' intConst)? ;


// lexer rules

// prefix area processing
COMMENT : {getCharPositionInLine() == 0}? 'C' ~('\n')* '\n'? -> skip ;
STMTNUM : {getCharPositionInLine() < 5}? [0-9]+ ;
CONTINUE : NEWCARD . . . . . ~[' '|'0'] -> skip ;

// body processing
ID      : {getCharPositionInLine() > 5}? [A-Z][A-Z0-9]* ;
NUMBER  : {getCharPositionInLine() > 5}? [0-9]+ ;
// return newlines to parser
NEWCARD : '\r'? '\n' ;
// skip spaces and tabs
WS      : ' '+ -> skip ;
