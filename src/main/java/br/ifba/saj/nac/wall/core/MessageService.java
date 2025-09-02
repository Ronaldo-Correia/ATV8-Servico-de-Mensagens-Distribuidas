package br.ifba.saj.nac.wall.core;

import br.ifba.saj.nac.wall.model.Message;
import br.ifba.saj.nac.wall.net.ReplicationClient;

import java.util.List;

public class MessageService {
    private final NodeState state;

    public MessageService(NodeState state) {
        this.state = state;
    }

    public void postMessage(String user, String text) {
        String id = generateUniqueId();
        long timestamp = System.currentTimeMillis();
        int lamport = state.bumpClock();
        String origin = state.getNodeId();

        Message msg = new Message(id, user, text, timestamp, lamport, origin);
        state.addMessage(msg); 
        replicate(msg);       
    }

    private void replicate(Message msg) {
    ReplicationClient client = new ReplicationClient(state.getPeers());
    client.sendMessages(List.of(msg));
}

    private String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }
}
