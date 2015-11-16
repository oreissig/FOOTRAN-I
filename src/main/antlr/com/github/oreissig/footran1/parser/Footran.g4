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
          // IF family
          | ifStatement
          | senseLight
          | ifSenseLight
          | ifSenseSwitch
          | ifAccumulatorOverflow
          | ifQuotientOverflow
          | ifDivideCheck
          // DO loop & misc control flow
          | doLoop
          | continueStmt
          | pause
          | stop
          // TODO I/O
          // specifications
          | dimension
          | equivalence
          | frequency
          ;

arithmeticFormula : (VAR_ID | FUNC_CANDIDATE | subscript) '=' expression;

uncondGoto   : 'GO' 'TO' statementNumber;
assignedGoto : 'GO' 'TO' variable ',' '(' statementNumber (',' statementNumber)* ')';
assign       : 'ASSIGN' statementNumber 'TO' variable;
computedGoto : 'GO' 'TO' '(' statementNumber (',' statementNumber)* ')' ',' variable;

ifStatement           : 'IF' '(' condition=expression ')' lessThan=statementNumber ','
                        equal=statementNumber ',' greaterThan=statementNumber;
senseLight            : 'SENSE' 'LIGHT' light=ufixedConst;
ifSenseLight          : 'IF' '(' 'SENSE' 'LIGHT' light=ufixedConst ')'
                        on=statementNumber ',' off=statementNumber;
ifSenseSwitch         : 'IF' '(' 'SENSE' 'SWITCH' senseSwitch=ufixedConst ')'
                        down=statementNumber ',' up=statementNumber;
ifAccumulatorOverflow : 'IF' 'ACCUMULATOR' 'OVERFLOW'
                        on=statementNumber ',' off=statementNumber;
ifQuotientOverflow    : 'IF' 'QUOTIENT' 'OVERFLOW'
                        on=statementNumber ',' off=statementNumber;
ifDivideCheck         : 'IF' 'DIVIDE' 'CHECK'
                        on=statementNumber ',' off=statementNumber;

doLoop       : 'DO' range=statementNumber index=variable '=' first=loopBoundary ','
               last=loopBoundary (',' step=loopBoundary)?;
loopBoundary : ufixedConst|variable;
continueStmt : 'CONTINUE';
pause        : 'PAUSE' consoleOutput=ufixedConst?;
stop         : 'STOP'  consoleOutput=ufixedConst?;

// TODO I/O rules

dimension   : 'DIMENSION' allocation (',' allocation)*;
allocation  : var=VAR_ID '(' ufixedConst (',' ufixedConst (',' ufixedConst)? )? ')';
equivalence : 'EQUIVALENCE' group (',' group)*;
group     : '(' quantity (',' quantity)+ ')';
quantity    : FUNC_CANDIDATE | VAR_ID ('(' location=ufixedConst ')')?;
frequency   : 'FREQUENCY' estimate (',' estimate)*;
estimate    : statementNumber '(' ufixedConst (',' ufixedConst)* ')';

variable   : VAR_ID | FUNC_CANDIDATE;
subscript  : var=VAR_ID '(' subscriptExpression (','
                            subscriptExpression (','
                            subscriptExpression )? )? ')';
subscriptExpression : constant=ufixedConst
                    | (factor=ufixedConst '*')? index=variable (sign summand=ufixedConst)?;
// equal to ufixedConst, but nice to separate semantically
statementNumber     : NUMBER;

expression : sign? unsigned=unsignedExpression;
unsignedExpression : '(' expression ')' | variable | subscript | functionCall | ufixedConst | ufloatConst;

functionCall : function=FUNC_CANDIDATE '(' expression (',' expression)* ')';

ufixedConst : NUMBER ;
ufloatConst : integer=NUMBER? '.' (fraction=NUMBER
              | fractionE=FLOAT_FRAC expSign=sign? exponent=ufixedConst)? ;

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
