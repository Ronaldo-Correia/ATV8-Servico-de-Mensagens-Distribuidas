package br.ifba.saj.nac.wall.core;

import java.util.List;
import br.ifba.saj.nac.wall.model.Message;
import br.ifba.saj.nac.wall.net.ReplicationClient;
import br.ifba.saj.nac.wall.auth.AuthService;

// Serviço responsável por postar e replicar mensagens
public class MessageService {
    private final NodeState state;
    private final AuthService auth;

    // Inicializa com estado do nó e AuthService
    public MessageService(NodeState state, AuthService auth) {
        this.state = state;
        this.auth = auth;
    }

    // Cria e envia mensagem para o mural e peers
    public void postMessage(String user, String text) {
        if (!auth.isAuthenticated(user)) {
            System.out.println("❌ Usuário não logado! Mensagem não enviada.");
            return;
        }

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
