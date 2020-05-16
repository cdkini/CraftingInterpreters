package kumquat.scanner;

import java.util.ArrayList;
import java.util.List;

import static kumquat.scanner.TokenType.*;

public class TokenScanner {

    private final String source;
    private final List<Token> tokens;
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

    public List<Token> scanTokens() {
        while (current < source.length()) {
            start = current;
            scan();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scan() {
        current++;
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!': addToken(match('=') ? BANG_EQUAL: BANG); break;
            case '=': addToken(match('=') ? EQUAL_EQUAL: EQUAL); break;
            case '<': addToken(match('=') ? LESS_EQUAL: LESS); break;
            case '>': addToken(match('=') ? GREATER_EQUAL: GREATER); break;
            case '/':
                if (match('/')) {
                    // A comment goes on until EOL
                    while (peek() != '\n' && current < source.length()) {
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t': break;
            case '\n': line++; break;
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

    private boolean match(char expected) {
        if (current >= source.length() || source.charAt(current) != expected) {
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

    // TODO: Where you left off

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
        while (peek() != '"' && current < source.length()) {
            if (peek() == '\n') {
                line++;
                advance();
            }
        }

        if (current >= source.length()) {
            Kumquat.error(line, "Unterminated string.");
            return;
        }

        advance();

        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private char peek() {
        if (current >= source.length()) {
            return '\0';
        }
        return source.charAt(current);
    }
}
