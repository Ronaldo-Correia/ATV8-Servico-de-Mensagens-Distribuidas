package br.ifba.saj.nac.wall.auth;

import java.util.Map;

public class AuthService {
    private Map<String, String> users;

    public AuthService(Map<String, String> users) {
        this.users = users;
    }

    public boolean authenticate(String user, String pass) {
        return users.containsKey(user) && users.get(user).equals(pass);
    }
}
