package footran.lexer;

public enum TokenType {
	// Expressions
	/** Left parenthesis */
    PAREN1,
    /** Right parenthesis */
    PAREN2,
    /** = operator */
    EQUALS,
    /** , separator */
    COMMA,
    /** Addition operator or positive sign */
    PLUS,
    /** Subtraction operator or negative sign */
    MINUS,
    /** Multiplication operator */
    MUL,
    /** Division operator */
    DIV,
    /** Exponentiation operator */
    EXP,
    
    // Control Statements
    // TODO merge GOTO into one token
    /** First part of GO TO keyword */
    GO,
    /** Second part of GO TO keyword */
    TO,
    /** ASSIGN keyword */
    ASSIGN,
    /** IF keyword */
    IF,
    /** SENSE keyword */
    SENSE,
    /** LIGHT keyword */
    LIGHT,
    /** SWITCH keyword */
    SWITCH,
    /** ACCUMULATOR keyword */
    ACCUMULATOR,
    /** OVERFLOW keyword */
    OVERFLOW,
    /** QUOTIENT keyword */
    QUOTIENT,
    /** DIVIDE keyword */
    DIVIDE,
    /** CHECK keyword */
    CHECK,
    /** PAUSE keyword */
    PAUSE,
    /** STOP keyword */
    STOP,
    /** DO keyword */
    DO,
    /** CONTINUE keyword */
    CONTINUE,
    
    // Input/Output
    /** READ keyword */
    READ,
    /** PUNCH keyword */
    PUNCH,
    /** PRINT keyword */
    PRINT,
    /** INPUT keyword */
    INPUT,
    /** WRITE keyword */
    WRITE,
    /** OUTPUT keyword */
    OUTPUT,
    /** FORMAT keyword */
    FORMAT,
    /** TAPE keyword */
    TAPE,
    /** DRUM keyword */
    DRUM,
    /** END keyword */
    END,
    /** FILE keyword */
    FILE,
    /** REWIND keyword */
    REWIND,
    /** BACKSPACE keyword */
    BACKSPACE,
    
    // Specification Statements
    /** DIMENSION keyword */
    DIMENSTION,
    /** EQUIVALENCE keyword */
    EQUIVALENCE,
    /** FREQUENCY keyword */
    FREQUENCY,
    
    // Literals
    // order is relevant in TokenImpl!
    /** Fixed Point Variable identifier */
    VAR_INT,
    /** Floating Point Variable identifier */
    VAR_FLOAT,
    /** Fixed Point Function identifier */
    FUNC_INT,
    /** Floating Point Function identifier */
    FUNC_FLOAT,
    /** Fixed Point Constant */
    CONST_INT,
    /** Octal Fixed Point Constant */
    CONST_OCTAL,
    /** Floating Point Constant */
    CONST_FLOAT
}