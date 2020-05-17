package rhubarb.parser;

import rhubarb.Rhubarb;

import java.util.ArrayList;
import java.util.List;
import static rhubarb.parser.TokenType.*;

public class TokenScanner {

    private final String source;
    private final List<Token> tokens;
    private int start; // First char in the lexeme being evaluated
    private int current; // Current char in the lexeme being evaluated
    private int line;
    private boolean inComment;

    public TokenScanner(String source) {
        this.source = source;
        this.tokens = new ArrayList<>();
        this.start = 0;
        this.current = 0;
        this.line = 1;
        this.inComment = false;
    }

    /**
     * Generates a list of tokens from a given source and assigns them to this..tokens
     * @return An ArrayList of tokens (this.tokens)
     */
    public List<Token> scanTokens() {
        while (current < source.length()) {
            start = current;
            scan();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    /**
     * The bulk of our scanner class, this helper method helps assign
     * sequences of characters to their appropriate TokenType.
     */
    private void scan() {
        char c = advance();
        switch (c) {

            // Single character tokens
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);

            // Tokens that can be either one or two characters
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            // If a comment, the rest of the line is advanced without being consumption
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && current < source.length()) {
                        advance();
                    }
                } else if (match('*')) {
                    // TODO: Fix multiline comment terminating condition
                    while (current < source.length()) {
                        if (peek() == '*' && peekNext() == '/') {
                            advance();
                        } else if (peek() == '\n') {
                            line++; // Disregard all characters but increment lines if we have a line break in comment
                        }
                        advance();
                    }
                } else {
                    addToken(SLASH);
                }
                break;

            // We don't care about whitespace so disregard if character is one of the following
            case ' ': break;
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
        String text = source.substring(start, current );
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
        addToken(STRING, value);
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
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void identifier() {
        while (Character.isAlphabetic(peek()) || Character.isDigit(peek())) {
            advance();
        }
        TokenType type = KeywordsDict.get(source.substring(start, current));
        if (type == null) {
            addToken(IDENTIFIER);
        } else {
            addToken(type);
        }
    }

    public int getLine() {
        return line;
    }
}
