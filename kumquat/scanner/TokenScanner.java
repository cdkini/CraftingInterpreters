package kumquat.scanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kumquat.scanner.TokenType.*;

public class TokenScanner {

    private final String source;
    private final List<Token> tokens;
    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }
    private int start;
    private int current;
    private int line;

    TokenScanner(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
        this.start = 0;
        this.current = 0;
        this.line = 1;
    }

    List<Token> scanTokens() {
        while (isAtEnd()) {
            start = current;
            scanTokens();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        current++;
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN); break;
            case ')':
                addToken(RIGHT_PAREN); break;
            case '{':
                addToken(LEFT_BRACE); break;
            case '}':
                addToken(RIGHT_BRACE); break;
            case ',':
                addToken(COMMA); break;
            case '.':
                addToken(DOT); break;
            case '-':
                addToken(MINUS); break;
            case '+':
                addToken(PLUS); break;
            case ';':
                addToken(SEMICOLON); break;
            case '*':
                addToken(STAR); break;
            case '!':
                addToken(match('=') ? BANG_EQUAL: BANG); break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL: EQUAL); break;
            case '<':
                addToken(match('=') ? LESS_EQUAL: LESS); break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL: GREATER); break;
            case '/':
                if (match('/')) {
                    // A comment goes on until EOL
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;

            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Kumquat.error(line, "Unexpected character.");
                    break;
                }
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        addToken(IDENTIFIER);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek())) {
                advance();
            }
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                advance();
            }
        }

        if (isAtEnd()) {
            Kumquat.error(line, "Unterminated string.");
            return;
        }

        advance();

        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private boolean match(char expected) {
        if (isAtEnd() || source.charAt(current) != expected) {
            return false;
        }
        current++;
        return true;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
