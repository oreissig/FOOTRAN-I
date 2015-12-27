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

// http://www.fortran.com/FortranForTheIBM704.pdf#9
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

// http://www.fortran.com/FortranForTheIBM704.pdf#18
arithmeticFormula : (VAR_ID | FUNC_CANDIDATE | subscript) '=' expression;


// http://www.fortran.com/FortranForTheIBM704.pdf#19
uncondGoto   : 'GO' 'TO' statementNumber;
assignedGoto : 'GO' 'TO' variable ',' '(' statementNumber (',' statementNumber)* ')';
assign       : 'ASSIGN' statementNumber 'TO' variable;
computedGoto : 'GO' 'TO' '(' statementNumber (',' statementNumber)* ')' ',' variable;


// http://www.fortran.com/FortranForTheIBM704.pdf#20
ifStatement           : 'IF' '(' condition=expression ')' lessThan=statementNumber ','
                        equal=statementNumber ',' greaterThan=statementNumber;
senseLight            : 'SENSE' 'LIGHT' light=ufixedConst;
ifSenseLight          : 'IF' '(' 'SENSE' 'LIGHT' light=ufixedConst ')'
                        on=statementNumber ',' off=statementNumber;
ifSenseSwitch         : 'IF' '(' 'SENSE' 'SWITCH' senseSwitch=ufixedConst ')'
                        down=statementNumber ',' up=statementNumber;
// http://www.fortran.com/FortranForTheIBM704.pdf#21
ifAccumulatorOverflow : 'IF' 'ACCUMULATOR' 'OVERFLOW'
                        on=statementNumber ',' off=statementNumber;
ifQuotientOverflow    : 'IF' 'QUOTIENT' 'OVERFLOW'
                        on=statementNumber ',' off=statementNumber;
ifDivideCheck         : 'IF' 'DIVIDE' 'CHECK'
                        on=statementNumber ',' off=statementNumber;
// equal to ufixedConst, but nice to separate semantically
statementNumber : NUMBER;


// http://www.fortran.com/FortranForTheIBM704.pdf#22
doLoop       : 'DO' range=statementNumber index=variable '=' first=loopBoundary ','
               last=loopBoundary (',' step=loopBoundary)?;
loopBoundary : ufixedConst|variable;
// http://www.fortran.com/FortranForTheIBM704.pdf#24
continueStmt : 'CONTINUE';
// http://www.fortran.com/FortranForTheIBM704.pdf#21
pause        : 'PAUSE' consoleOutput=ufixedConst?;
stop         : 'STOP'  consoleOutput=ufixedConst?;


// TODO I/O rules
// http://www.fortran.com/FortranForTheIBM704.pdf#26


// http://www.fortran.com/FortranForTheIBM704.pdf#37
dimension   : 'DIMENSION' allocation (',' allocation)*;
allocation  : var=VAR_ID '(' ufixedConst (',' ufixedConst (',' ufixedConst)? )? ')';
// http://www.fortran.com/FortranForTheIBM704.pdf#38
equivalence : 'EQUIVALENCE' group (',' group)*;
group     : '(' quantity (',' quantity)+ ')';
quantity    : FUNC_CANDIDATE | VAR_ID ('(' location=ufixedConst ')')?;
// http://www.fortran.com/FortranForTheIBM704.pdf#39
frequency   : 'FREQUENCY' estimate (',' estimate)*;
estimate    : statementNumber '(' ufixedConst (',' ufixedConst)* ')';


// http://www.fortran.com/FortranForTheIBM704.pdf#12
variable   : VAR_ID | FUNC_CANDIDATE;
// http://www.fortran.com/FortranForTheIBM704.pdf#13
subscript  : var=VAR_ID '(' subscriptExpression (','
                            subscriptExpression (','
                            subscriptExpression )? )? ')';
subscriptExpression : constant=ufixedConst
                    | (factor=ufixedConst '*')? index=variable (sign summand=ufixedConst)?;


// http://www.fortran.com/FortranForTheIBM704.pdf#16
expression : sign? sum;
sum        : sum sumOp=sign sum
           | product;
product    : product mulOp product
           | power;
power      : unaryExpression (POWER unaryExpression)?;
unaryExpression : variable | subscript | functionCall | ufixedConst | ufloatConst
                | '(' expression ')';

// http://www.fortran.com/FortranForTheIBM704.pdf#14
functionCall : function=FUNC_CANDIDATE '(' expression (',' expression)* ')';

// http://www.fortran.com/FortranForTheIBM704.pdf#11
ufixedConst : NUMBER ;
ufloatConst : integer=NUMBER? '.' (fraction=NUMBER
              | fractionE=FLOAT_FRAC expSign=sign? exponent=ufixedConst)? ;

sign  : (PLUS|MINUS);
mulOp : (MUL|DIV);


// lexer rules

// prefix area processing
// http://www.fortran.com/FortranForTheIBM704.pdf#10
COMMENT  : {getCharPositionInLine() == 0}? 'C' ~('\n')* '\n'? -> skip ;
STMTNUM  : {getCharPositionInLine() < 5}? [1-9][0-9]* ;
// http://www.fortran.com/FortranForTheIBM704.pdf#9
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
