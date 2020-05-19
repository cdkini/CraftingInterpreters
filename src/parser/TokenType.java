package src.parser;

public enum TokenType {
    // Single-character tokens.
    L_PAREN, R_PAREN, L_BRACE, R_BRACE, L_BRACK, R_BRACK,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH,

    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL, STAR,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, CLASS, ELSE, FALSE, FUNC, FOR, IF, NULL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR,

    EOF
}
