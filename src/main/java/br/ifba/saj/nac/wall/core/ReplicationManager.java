package br.ifba.saj.nac.wall.core;

import java.util.Collection;

import br.ifba.saj.nac.wall.model.Message;

// Gerencia replicação e reconciliação de mensagens
public class ReplicationManager {
    private final NodeState state;

    public ReplicationManager(NodeState state) {
        this.state = state;
    }

    // (Não implementado) Replicação de mensagem
    public void replicateMessage(Message msg) {
    }

    // Reconcilia mensagens recebidas de peers
    public void reconcile(Collection<Message> incoming) {
        for (Message m : incoming) {
            state.addMessage(m);
        }
    }
}
