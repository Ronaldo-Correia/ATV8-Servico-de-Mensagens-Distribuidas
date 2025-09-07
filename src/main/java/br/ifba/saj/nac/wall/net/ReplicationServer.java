package br.ifba.saj.nac.wall.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

import br.ifba.saj.nac.wall.core.NodeState;
import br.ifba.saj.nac.wall.model.Message;

// Servidor que recebe mensagens de replicação de peers
public class ReplicationServer implements Runnable {
    private final NodeState state;
    private final int port;

    public ReplicationServer(NodeState state, int port) {
        this.state = state;
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("ReplicationServer rodando na porta " + port);
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handleClient(client)).start(); // cada conexão em uma thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Processa mensagens recebidas do peer
    private void handleClient(Socket client) {
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {

            if (state.isFailMode())
                return; // ignora se nó está em falha

            Object received = in.readObject();
            if (received instanceof Collection<?>) {
                @SuppressWarnings("unchecked")
                Collection<Message> messages = (Collection<Message>) received;
                for (Message msg : messages) {
                    state.addMessage(msg); // adiciona mensagens recebidas
                }
                out.writeObject(state.getMessages()); // responde com mensagens locais
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
