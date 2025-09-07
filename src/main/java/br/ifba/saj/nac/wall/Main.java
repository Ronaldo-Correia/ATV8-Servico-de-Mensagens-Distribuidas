package br.ifba.saj.nac.wall;

import java.util.Arrays;
import java.util.List;

import br.ifba.saj.nac.wall.core.MessageService; // ✅ IMPORTADO
import br.ifba.saj.nac.wall.core.NodeState;
import br.ifba.saj.nac.wall.core.ReplicationManager;
import br.ifba.saj.nac.wall.net.ReplicationServer;
import br.ifba.saj.nac.wall.replication.AntiEntropyTask;
import br.ifba.saj.nac.wall.simulation.FailureSimulator;

public class Main {
    public static void main(String[] args) throws Exception {
        // Verifica argumentos necessários
        if (args.length < 3) {
            System.err.println("Usage: Main <nodeId> <port> <peer1Host:peer1Port,...>");
            System.exit(1);
        }

        String nodeId = args[0];
        int port = Integer.parseInt(args[1]);
        String peers = args[2];

        // Cria estado do nó
        NodeState node = new NodeState(nodeId);
        List<String> peerList = Arrays.asList(peers.split(","));
        node.setPeers(peerList);

        // Gerenciador de replicação e simulador de falha
        ReplicationManager manager = new ReplicationManager(node);
        FailureSimulator sim = new FailureSimulator(node);

        // Inicia servidor de replicação em thread separada
        Thread serverThread = new Thread(new ReplicationServer(node, port));
        serverThread.start();

        // Inicia tarefa de anti-entropia (sincronização periódica)
        Thread aet = new Thread(new AntiEntropyTask(node, peers));
        aet.setDaemon(true);
        aet.start();

        System.out.println(nodeId + " rodando na porta " + port + " com peers=" + peers);

        // Serviço de mensagens para postar no mural
        MessageService service = new MessageService(node);

        // Thread para entrada de comandos pelo usuário
        Thread inputThread = new Thread(() -> {
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            while (true) {
                System.out.print("Comando (fail/recover/show/post): ");
                String cmd = scanner.nextLine().trim().toLowerCase();

                switch (cmd) {
                    case "fail":
                        node.setFailMode(true);
                        System.out.println("⚠️ Nodo em modo de falha!");
                        break;
                    case "recover":
                        node.setFailMode(false);
                        System.out.println("✅ Nodo recuperado!");
                        break;
                    case "show":
                        // Exibe mensagens armazenadas
                        System.out.println("Mensagens armazenadas:");
                        for (var m : node.getMessages()) {
                            System.out.println(m.getUser() + ": " + m.getText() + " [" + m.getLamport() + "]");
                        }
                        break;
                    case "post":
                        // Posta nova mensagem no mural
                        System.out.print("Usuário: ");
                        String user = scanner.nextLine();
                        System.out.print("Mensagem: ");
                        String text = scanner.nextLine();
                        service.postMessage(user, text);
                        System.out.println("✅ Mensagem enviada!");
                        break;
                    default:
                        System.out.println("Comando inválido.");
                }
            }
        });

        inputThread.setDaemon(true);
        inputThread.start();

        // Mantém a thread principal viva
        Thread.currentThread().join();
    }
}
