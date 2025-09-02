package br.ifba.saj.nac.wall.replication;

import br.ifba.saj.nac.wall.core.NodeState;
import br.ifba.saj.nac.wall.core.ReplicationManager;
import br.ifba.saj.nac.wall.model.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;

public class AntiEntropyTask implements Runnable {
    private final NodeState state;
    private final String peersCsv;

    public AntiEntropyTask(NodeState state, String peersCsv) {
        this.state = state;
        this.peersCsv = peersCsv;
    }

    @Override
    public void run() {
        ReplicationManager manager = new ReplicationManager(state);
        String[] peers = peersCsv.split(",");

        while (true) {
            try {
                if (!state.isFailMode()) {
                    for (String peer : peers) {
                        String[] parts = peer.split(":");
                        String host = parts[0];
                        int port = Integer.parseInt(parts[1]);

                        try (Socket socket = new Socket(host, port);
                             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                            Collection<Message> messages = state.getMessages();
                            out.writeObject(messages);
                            out.flush();

                            Object response = in.readObject();
                            if (response instanceof Collection<?>) {
                                @SuppressWarnings("unchecked")
                                Collection<Message> incoming = (Collection<Message>) response;
                                manager.reconcile(incoming);
                                System.out.println("🔁 Reconciliação com " + peer + ": recebidas " + incoming.size() + " msgs.");
                            }

                        } catch (Exception e) {
                            System.out.println("Erro ao conectar com " + peer);
                        }
                    }
                }

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
