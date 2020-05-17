package rhubarb.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import rhubarb.Token;
import rhubarb.TokenScanner;
import static rhubarb.TokenType.*;

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
        TokenScanner ts = new TokenScanner("\"rhubarb\"");
        List<Token> tokenList = ts.scanTokens();
        assertEquals(2, tokenList.size());
        assertEquals(IDENTIFIER, tokenList.get(0).getType());
    }

    @Test
    public void testScanOfInvalidString() {
        // TODO: Fill out test
        TokenScanner ts = new TokenScanner("\"rhubarb");
        List<Token> tokenList = ts.scanTokens();
    }

    @Test
    public void testScanOfNumber() {
        // TODO: Fill out test
        TokenScanner ts = new TokenScanner("");
        List<Token> tokenList = ts.scanTokens();
    }

    @Test
    public void testScanOfKeyword() {
        // TODO: Fill out test
        TokenScanner ts = new TokenScanner("");
        List<Token> tokenList = ts.scanTokens();
    }

    @Test
    public void testScanOfVariableName() {
        // TODO: Fill out test
        TokenScanner ts = new TokenScanner("");
        List<Token> tokenList = ts.scanTokens();
    }

    @Test
    public void testScanOfInvalidCharacter() {
        // TODO: Fill out test
        TokenScanner ts = new TokenScanner("");
        List<Token> tokenList = ts.scanTokens();
    }

    @Test
    public void testScanOfSeriesOfLexemes1() {
        // TODO: Fill out test
        TokenScanner ts = new TokenScanner("");
        List<Token> tokenList = ts.scanTokens();
    }

    @Test
    public void testScanOfSeriesOfLexemes2() {
        // TODO: Fill out test
        TokenScanner ts = new TokenScanner("");
        List<Token> tokenList = ts.scanTokens();
    }

    @Test
    public void testScanOfSeriesOfLexemes3() {
        // TODO: Fill out test
        TokenScanner ts = new TokenScanner("");
        List<Token> tokenList = ts.scanTokens();
    }
}
