package br.ifba.saj.nac.wall.simulation;

import br.ifba.saj.nac.wall.core.NodeState;

public class FailureSimulator {
    private final NodeState state;

    public FailureSimulator(NodeState state) {
        this.state = state;
    }

    public void failNode() {
        state.setFailMode(true);
        System.out.println(state.getNodeId() + " entrou em modo falha.");
    }

    public void recoverNode() {
        state.setFailMode(false);
        System.out.println(state.getNodeId() + " recuperou operação.");
    }
}
