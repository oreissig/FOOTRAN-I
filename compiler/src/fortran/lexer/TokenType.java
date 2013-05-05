package fortran.lexer;

public enum TokenType {
	// Expressions
    BRACE1, BRACE2,
    EQUALS, COMMA,
    PLUS, MINUS, MUL, DIV, EXP,
    // Control Statements
    GO, TO, ASSIGN, IF,
    SENSE, LIGHT, SWITCH,
    ACCUMULATOR, OVERFLOW, QUOTIENT, DIVIDE, CHECK,
    PAUSE, STOP, DO, CONTINUE,
    // Input/Output
    READ, PUNCH, PRINT, INPUT,
    WRITE, OUTPUT,
    FORMAT,
    TAPE, DRUM,
    END, FILE, REWIND, BACKSPACE,
    // Specification Statements
    DIMENSTION, EQUIVALENCE, FREQUENCY,
    // Literals
    IDENT, CONST_INT, CONST_FLOAT
}