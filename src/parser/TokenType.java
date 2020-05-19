package src.parser;

public enum TokenType {
    // Single-character tokens.
    LPAREN, RPAREN, LBRACE, RBRACE, LBRACK, RBRACK,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH,

    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL, STAR,

    // Literals.
    ID, STRING, NUMBER,

    // Keywords.
    AND, CLASS, ELSE, FALSE, FUNC, FOR, IF, NULL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR,

    EOF
}
