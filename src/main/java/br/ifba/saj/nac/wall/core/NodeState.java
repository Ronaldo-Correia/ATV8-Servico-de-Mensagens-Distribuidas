package br.ifba.saj.nac.wall.core;

import br.ifba.saj.nac.wall.model.Message;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeState {
    private final String nodeId;
    private int lamportClock = 0;
    private volatile boolean failMode = false;
    private final Map<String, Message> messages = new ConcurrentHashMap<>();
    private List<String> peers;


    public NodeState(String nodeId) {
        this.nodeId = nodeId;
    }

    public synchronized int bumpClock() {
        return ++lamportClock;
    }

    public synchronized void updateClock(int otherClock) {
        lamportClock = Math.max(lamportClock, otherClock) + 1;
    }

    public synchronized boolean addMessage(Message msg) {
        if (messages.containsKey(msg.getId())) return false;
        messages.put(msg.getId(), msg);
        updateClock(msg.getLamport());
        return true;
    }

    public Collection<Message> getMessages() {
        return messages.values();
    }

    public boolean isFailMode() {
        return failMode;
    }

    public void setFailMode(boolean failMode) {
        this.failMode = failMode;
    }

    public String getNodeId() {
        return nodeId;
    }
    
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
