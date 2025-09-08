package br.ifba.saj.nac.wall.auth;

import java.util.*;

// Serviço simples de autenticação de usuários com estado de login
public class AuthService {
    private final Map<String, String> users;
    private final Set<String> loggedUsers = new HashSet<>();

    public AuthService(Map<String, String> users) {
        this.users = users;
    }

    // Tenta autenticar o usuário
    public boolean login(String user, String pass) {
        if (users.containsKey(user) && users.get(user).equals(pass)) {
            loggedUsers.add(user);
            return true;
        }
        return false;
    }

    // Verifica se o usuário já está logado
    public boolean isAuthenticated(String user) {
        return loggedUsers.contains(user);
    }

    // Permitir logout
    public void logout(String user) {
        loggedUsers.remove(user);
    }

    // Verifica se o usuário existe
    public boolean userExists(String user) {
        return users.containsKey(user);
    }

    // Registra um novo usuário
    public void register(String user, String pass) {
        users.put(user, pass);
    }

}
