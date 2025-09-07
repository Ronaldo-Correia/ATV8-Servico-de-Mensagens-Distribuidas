package br.ifba.saj.nac.wall.auth;

import java.util.Map;

// Serviço simples de autenticação de usuários
public class AuthService {
    private Map<String, String> users;

    public AuthService(Map<String, String> users) {
        this.users = users;
    }

    // Verifica usuário e senha
    public boolean authenticate(String user, String pass) {
        return users.containsKey(user) && users.get(user).equals(pass);
    }
}
