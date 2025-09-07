package br.ifba.saj.nac.wall.core;

import java.util.List;

import br.ifba.saj.nac.wall.model.Message;
import br.ifba.saj.nac.wall.net.ReplicationClient;

// Serviço responsável por postar e replicar mensagens
public class MessageService {
    private final NodeState state;

    // Inicializa com estado do nó
    public MessageService(NodeState state) {
        this.state = state;
    }

    // Cria e envia mensagem para o mural e peers
    public void postMessage(String user, String text) {
        String id = generateUniqueId();
        long timestamp = System.currentTimeMillis();
        int lamport = state.bumpClock();
        String origin = state.getNodeId();

        Message msg = new Message(id, user, text, timestamp, lamport, origin);
        state.addMessage(msg); // adiciona localmente
        replicate(msg); // replica para peers
    }

    // Replica mensagem para todos os peers
    private void replicate(Message msg) {
        ReplicationClient client = new ReplicationClient(state.getPeers());
        client.sendMessages(List.of(msg));
    }

    // Gera identificador único para mensagem
    private String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }
}
