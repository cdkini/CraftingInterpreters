package rhubarb.parser;

import java.util.HashMap;
import java.util.Map;

public class KeywordsDict {

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("func", TokenType.FUNC);
        keywords.put("if", TokenType.IF);
        keywords.put("null", TokenType.NULL);
        keywords.put("or", TokenType.OR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while", TokenType.WHILE);
    }

    public static TokenType get(String key) {
        if (!keywords.containsKey(key)) {
            return null;
        }
        return keywords.get(key);
    }
}
