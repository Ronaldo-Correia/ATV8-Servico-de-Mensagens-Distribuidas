package br.ifba.saj.nac.wall.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.ifba.saj.nac.wall.model.Message;

// Estado do nó: mensagens, relógio Lamport, peers, modo falha
public class NodeState {
    private final String nodeId;
    private int lamportClock = 0;
    private volatile boolean failMode = false;
    private final Map<String, Message> messages = new ConcurrentHashMap<>();
    private List<String> peers;

    public NodeState(String nodeId) {
        this.nodeId = nodeId;
    }

    // Incrementa relógio Lamport
    public synchronized int bumpClock() {
        return ++lamportClock;
    }

    // Atualiza relógio Lamport com valor recebido
    public synchronized void updateClock(int otherClock) {
        lamportClock = Math.max(lamportClock, otherClock) + 1;
    }

    // Adiciona mensagem se não existe
    public synchronized boolean addMessage(Message msg) {
        if (messages.containsKey(msg.getId()))
            return false;
        messages.put(msg.getId(), msg);
        updateClock(msg.getLamport());
        return true;
    }

    // Retorna mensagens armazenadas
    public Collection<Message> getMessages() {
        return messages.values();
    }

    // Modo falha do nó
    public boolean isFailMode() {
        return failMode;
    }

    public void setFailMode(boolean failMode) {
        this.failMode = failMode;
    }

    public String getNodeId() {
        return nodeId;
    }

    // Peers conectados
    public void setPeers(List<String> peers) {
        this.peers = peers;
    }

    public List<String> getPeers() {
        return peers;
    }

    public int getLamportClock() {
        return lamportClock;
    }
}
