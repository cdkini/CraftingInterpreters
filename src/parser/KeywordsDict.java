package src.parser;

import java.util.HashMap;
import java.util.Map;
import static src.parser.TokenType.*;

public class KeywordsDict {

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("func", FUNC);
        keywords.put("if", IF);
        keywords.put("null", NULL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
    }

    public static TokenType get(String key) {
        if (!keywords.containsKey(key)) {
            return null;
        }
        return keywords.get(key);
    }
}
