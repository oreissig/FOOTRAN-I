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
STMTNUM : {getCharPositionInLine() < 6}? [0-9]+ ;
CONTINUE : '\n' . . . . . ~[' '|'0'] -> skip ;

// body processing
ID      : {getCharPositionInLine() > 6}? [A-Z][A-Z0-9]+ ;
NUMBER  : {getCharPositionInLine() > 6}? [0-9]+ ;
// return newlines to parser
NEWCARD : '\r'? '\n' ;
// skip spaces and tabs
WS      : [ \t]+ -> skip ;
