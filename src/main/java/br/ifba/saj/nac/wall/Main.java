package br.ifba.saj.nac.wall;

import java.util.*;
import br.ifba.saj.nac.wall.core.MessageService;
import br.ifba.saj.nac.wall.core.NodeState;
import br.ifba.saj.nac.wall.core.ReplicationManager;
import br.ifba.saj.nac.wall.net.ReplicationServer;
import br.ifba.saj.nac.wall.replication.AntiEntropyTask;
import br.ifba.saj.nac.wall.simulation.FailureSimulator;
import br.ifba.saj.nac.wall.auth.AuthService;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: Main <nodeId> <port> <peer1Host:peer1Port,...>");
            System.exit(1);
        }

        String nodeId = args[0];
        int port = Integer.parseInt(args[1]);
        String peers = args[2];

        NodeState node = new NodeState(nodeId);
        List<String> peerList = Arrays.asList(peers.split(","));
        node.setPeers(peerList);

        ReplicationManager manager = new ReplicationManager(node);
        FailureSimulator sim = new FailureSimulator(node);

        Thread serverThread = new Thread(new ReplicationServer(node, port));
        serverThread.start();

        Thread aet = new Thread(new AntiEntropyTask(node, peers));
        aet.setDaemon(true);
        aet.start();

        System.out.println(nodeId + " rodando na porta " + port + " com peers=" + peers);

        // Usuários iniciais
        Map<String, String> users = new HashMap<>();
        users.put("alice", "123");
        users.put("bob", "abc");
        users.put("carol", "xyz");
        AuthService auth = new AuthService(users);

        // Serviço de mensagens
        MessageService service = new MessageService(node, auth);

        // CLI de comandos
        Thread inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Comando (register/login/post/logout/show/fail/recover): ");
                String cmd = scanner.nextLine().trim().toLowerCase();

                switch (cmd) {
                    case "register":
                        System.out.print("Novo usuário: ");
                        String newUser = scanner.nextLine();
                        if (auth.userExists(newUser)) {
                            System.out.println("❌ Usuário já existe!");
                            break;
                        }
                        System.out.print("Senha: ");
                        String newPass = scanner.nextLine();
                        auth.register(newUser, newPass);
                        System.out.println("✅ Usuário registrado com sucesso!");
                        break;

                    case "login":
                        System.out.print("Usuário: ");
                        String loginUser = scanner.nextLine();
                        System.out.print("Senha: ");
                        String loginPass = scanner.nextLine();
                        if (auth.login(loginUser, loginPass)) {
                            System.out.println("🔓 Login bem-sucedido!");
                        } else {
                            System.out.println("❌ Usuário ou senha inválidos!");
                        }
                        break;

                    case "post":
                        System.out.print("Usuário: ");
                        String userPost = scanner.nextLine();
                        if (!auth.isAuthenticated(userPost)) {
                            System.out.println("❌ Você precisa estar logado para postar!");
                            break;
                        }
                        System.out.print("Mensagem: ");
                        String text = scanner.nextLine();
                        service.postMessage(userPost, text);
                        System.out.println("✅ Mensagem enviada!");
                        break;

                    case "logout":
                        System.out.print("Usuário: ");
                        String u = scanner.nextLine();
                        auth.logout(u);
                        System.out.println("🔒 Logout realizado!");
                        break;

                    case "show":
                        System.out.println("Mensagens armazenadas:");
                        for (var m : node.getMessages()) {
                            System.out.println(m.getUser() + ": " + m.getText() + " [" + m.getLamport() + "]");
                        }
                        break;

                    case "fail":
                        node.setFailMode(true);
                        System.out.println("⚠️ Nodo em modo de falha!");
                        break;

                    case "recover":
                        node.setFailMode(false);
                        System.out.println("✅ Nodo recuperado!");
                        break;

                    default:
                        System.out.println("❌ Comando inválido.");
                        break;
                }
            }
        });

        inputThread.setDaemon(true);
        inputThread.start();

        Thread.currentThread().join();
    }
}
