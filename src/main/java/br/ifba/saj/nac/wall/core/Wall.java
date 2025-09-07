package br.ifba.saj.nac.wall.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ifba.saj.nac.wall.model.Message;

// Estrutura para armazenar mensagens do mural
public class Wall {
    private Map<String, Message> messages = new HashMap<>();

    // Adiciona mensagem se não existir
    public synchronized boolean addMessage(Message m) {
        if (messages.containsKey(m.getId()))
            return false;
        messages.put(m.getId(), m);
        return true;
    }

    // Retorna lista de mensagens do mural
    public synchronized List<Message> getMessages() {
        return new ArrayList<>(messages.values());
    }
}
