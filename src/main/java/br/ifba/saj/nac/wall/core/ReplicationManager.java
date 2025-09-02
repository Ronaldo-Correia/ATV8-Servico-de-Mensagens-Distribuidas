package br.ifba.saj.nac.wall.core;

import br.ifba.saj.nac.wall.model.Message;
import java.util.Collection;


public class ReplicationManager {
    private final NodeState state;

    public ReplicationManager(NodeState state) {
        this.state = state;
    }

    public void replicateMessage(Message msg) {
    }

    public void reconcile(Collection<Message> incoming) {
        for (Message m : incoming) {
            state.addMessage(m); 
        }
    }
}
