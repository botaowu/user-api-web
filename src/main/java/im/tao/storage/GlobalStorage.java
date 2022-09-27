package im.tao.storage;

import im.tao.entity.Role;
import im.tao.entity.Token;
import im.tao.entity.User;

import java.util.HashMap;
import java.util.Map;

public class GlobalStorage {
    private static final Map<String, User> USERS = new HashMap<>();

    private static final Map<String, Role> ROLES = new HashMap<>();

    private static final Map<String, Token> TOKENS = new HashMap<>();

    public synchronized static Map<String, User> getUsers() {
        return USERS;
    }

    public synchronized static Map<String, Role> getRoles() {
        return ROLES;
    }

    public synchronized static Map<String, Token> getTokens() {
        return TOKENS;
    }
}
