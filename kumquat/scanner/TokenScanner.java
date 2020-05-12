package kumquat.scanner;

import javax.management.BadAttributeValueExpException;
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
        char c = source.charAt(current - 1);
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
                Kumquat.error(line, "Unexpected character.");
                break;
        }
    }

    private void string() {
        while (peek() != '"' && !isAtEnd())
            // TODO: Left off here!
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
