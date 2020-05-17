package rhubarb.tests;

import static org.junit.Assert.*;
import static rhubarb.TokenType.*;

import org.junit.Test;
import rhubarb.Token;
import rhubarb.TokenScanner;
import java.util.List;

public class TestTokenScanner {

    @Test
    public void testScanOfSingleCharacter() {
        TokenScanner ts = new TokenScanner("((()");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(5, tokenList.size());
        assertEquals(LEFT_PAREN, tokenList.get(0).getType());
        assertEquals(RIGHT_PAREN, tokenList.get(3).getType());
        assertEquals(EOF, tokenList.get(4).getType());
    }

    @Test
    public void testScanOfAssignmentOperator() {
        TokenScanner ts = new TokenScanner("=");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(2, tokenList.size());
        assertEquals(EQUAL, tokenList.get(0).getType());
        assertEquals(EOF, tokenList.get(1).getType());
    }

    @Test
    public void testScanOfComparisonOperators() {
        TokenScanner ts = new TokenScanner(">=");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(2, tokenList.size());
        assertEquals(GREATER_EQUAL, tokenList.get(0).getType());
        assertEquals(EOF, tokenList.get(1).getType());
    }

    @Test
    public void testScanOfComment() {
        TokenScanner ts = new TokenScanner("//Text");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(1, tokenList.size());
    }

    @Test
    public void testScanOfWhitespace() {
        TokenScanner ts = new TokenScanner("  \t \r \n \n");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(1, tokenList.size());
        assertEquals(3, ts.getLine());
    }

    @Test
    public void testScanOfValidString() {
        // TODO: Fill out test (having trouble getting quotation marks to be recognized within test string)
    }

    @Test
    public void testScanOfInvalidString() {
        // TODO: Fill out test (having trouble getting quotation marks to be recognized within test string)
    }

    @Test
    public void testScanOfInteger() {
        TokenScanner ts = new TokenScanner("123");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(2, tokenList.size());
        assertEquals(NUMBER, ts.scanTokens().get(0).getType());
    }

    @Test
    public void testScanOfFloat() {
        TokenScanner ts = new TokenScanner("3.1415");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(2, tokenList.size());
        assertEquals(NUMBER, ts.scanTokens().get(0).getType());
    }

    @Test
    public void testScanOfKeyword() {
        TokenScanner ts = new TokenScanner("var");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(2, tokenList.size());
        assertEquals(VAR, ts.scanTokens().get(0).getType());
    }

    @Test
    public void testScanOfVariableName() {
        TokenScanner ts = new TokenScanner("python");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(2, tokenList.size());
        assertEquals(IDENTIFIER, ts.scanTokens().get(0).getType());
    }

    @Test
    public void testScanOfInvalidCharacter() {
        TokenScanner ts = new TokenScanner("`");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(1, tokenList.size());
        assertEquals(EOF, ts.scanTokens().get(0).getType());
    }

    @Test
    public void testScanOfSeriesOfLexemes1() {
        TokenScanner ts = new TokenScanner("var x = 23 \n \t x == y + 1");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(10, tokenList.size());
        assertEquals(VAR, ts.scanTokens().get(0).getType());
        assertEquals(EQUAL, ts.scanTokens().get(2).getType());
        assertEquals(NUMBER, ts.scanTokens().get(3).getType());
        assertEquals(PLUS, ts.scanTokens().get(7).getType());
        assertEquals(2, ts.getLine());
    }
}
