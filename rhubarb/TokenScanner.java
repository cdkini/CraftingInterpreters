package rhubarb;

import java.util.ArrayList;
import java.util.List;

public class TokenScanner {

    private final String source;
    private final List<Token> tokens;
    private int start; // First char in the lexeme being evaluated
    private int current; // Current char in the lexeme being evaluated
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
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scan() {

        current++;
        char c = advance();

        switch (c) {

            // Single character tokens
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;

            // Tokens that can be either one or two characters
            case '!': addToken(match('=') ? TokenType.BANG_EQUAL: TokenType.BANG); break;
            case '=': addToken(match('=') ? TokenType.EQUAL_EQUAL: TokenType.EQUAL); break;
            case '<': addToken(match('=') ? TokenType.LESS_EQUAL: TokenType.LESS); break;
            case '>': addToken(match('=') ? TokenType.GREATER_EQUAL: TokenType.GREATER); break;

            // If a comment (//), the rest of the line is advanced without being consumption
            case '/':
                if (match('/')) {
                    // A comment goes on until EOL
                    while (peek() != '\n' && current < source.length()) {
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
                break;

            // We don't care about whitespace so disregard if character is one of the following
            case ' ':
            case '\r':
            case '\t': break;
            case '\n': line++; break;

            // Remainder are delegated to string/number specific helper methods or error handling
            case '"':
                string();
                break;

            default:
                if (Character.isDigit(c)) {
                    number();
                } else if (Character.isAlphabetic(c)) {
                    identifier();
                } else {
                    Rhubarb.error(line, "Invalid character.");
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

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private char peek() {
        if (current >= source.length()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    private void string() {
        while (peek() != '"' && current < source.length()) {
            if (peek() == '\n') {
                line++;
                advance();
            }
        }
        if (current >= source.length()) {
            Rhubarb.error(line, "Unterminated string.");
            return;
        }
        advance();
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private void number() {
        while (Character.isDigit(peek())) {
            advance();
        }
        if (peek() == '.' && Character.isDigit(peekNext())) {
            advance();
            while (Character.isDigit(peek())) {
                advance();
            }
        }
        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void identifier() {
        while (Character.isAlphabetic(peek()) || Character.isDigit(peek())) {
            advance();
        }
        TokenType type = KeywordsDict.get(source.substring(start, current));
        if (type == null) {
            addToken(TokenType.IDENTIFIER);
        } else {
            addToken(type);
        }
    }
}
