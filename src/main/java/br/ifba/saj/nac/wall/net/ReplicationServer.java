package br.ifba.saj.nac.wall.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

import br.ifba.saj.nac.wall.core.NodeState;
import br.ifba.saj.nac.wall.model.Message;

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
                new Thread(() -> handleClient(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket client) {
    try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
         ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {

        if (state.isFailMode()) return;

        Object received = in.readObject();
        if (received instanceof Collection<?>) {
            @SuppressWarnings("unchecked")
            Collection<Message> messages = (Collection<Message>) received;
            for (Message msg : messages) {
                state.addMessage(msg);
            }
            out.writeObject(state.getMessages());
            out.flush();
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
