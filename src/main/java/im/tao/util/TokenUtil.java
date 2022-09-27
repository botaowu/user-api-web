package im.tao.util;

import im.tao.entity.Token;
import im.tao.storage.GlobalStorage;

import java.util.Map;

public class TokenUtil {

    public static Token getToken(String content) {
        Token token = null;

        Map<String, Token> tokens = GlobalStorage.getTokens();
        for (String key: tokens.keySet()) {
            Token t = tokens.get(key);
            if (content != null && content.equals(t.getContent())) {
                token = t;
                break;
            }
        }

        return token;
    }

}
