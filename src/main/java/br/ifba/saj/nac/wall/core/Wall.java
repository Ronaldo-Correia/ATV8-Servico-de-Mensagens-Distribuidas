package br.ifba.saj.nac.wall.core;

import java.util.*;

import br.ifba.saj.nac.wall.model.Message;

public class Wall {
    private Map<String, Message> messages = new HashMap<>();

    public synchronized boolean addMessage(Message m) {
        if (messages.containsKey(m.getId())) return false;
        messages.put(m.getId(), m);
        return true;
    }

    public synchronized List<Message> getMessages() {
        return new ArrayList<>(messages.values());
    }
}
