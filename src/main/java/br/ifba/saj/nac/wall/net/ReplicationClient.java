package br.ifba.saj.nac.wall.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;

import br.ifba.saj.nac.wall.model.Message;

// Cliente responsável por enviar mensagens de replicação para peers
public class ReplicationClient {
    private final Collection<String> peers;

    // Inicializa com lista de peers
    public ReplicationClient(Collection<String> peers) {
        this.peers = peers;
    }

    // Envia mensagens para todos os peers
    public void sendMessages(Collection<Message> messages) {
        for (String peer : peers) {
            String[] parts = peer.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);

            try (Socket socket = new Socket(host, port);
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                out.writeObject(messages); // envia mensagens
                out.flush();

                // Opcional: processar resposta do peer
                Object response = in.readObject();

            } catch (Exception e) {
                System.err.println("Erro ao conectar com peer " + peer);
                e.printStackTrace();
            }
        }
    }
}
