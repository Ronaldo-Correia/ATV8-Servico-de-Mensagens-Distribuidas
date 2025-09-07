package br.ifba.saj.nac.wall.simulation;

import br.ifba.saj.nac.wall.core.NodeState;

// Simula falha e recuperação do nó
public class FailureSimulator {
    private final NodeState state;

    // Inicializa com estado do nó
    public FailureSimulator(NodeState state) {
        this.state = state;
    }

    // Coloca nó em modo de falha
    public void failNode() {
        state.setFailMode(true);
        System.out.println(state.getNodeId() + " entrou em modo falha.");
    }

    // Recupera nó do modo de falha
    public void recoverNode() {
        state.setFailMode(false);
        System.out.println(state.getNodeId() + " recuperou operação.");
    }
}
