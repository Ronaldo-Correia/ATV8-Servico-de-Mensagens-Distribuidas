package br.ifba.saj.nac.wall.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Node {
    private String id;
    private boolean failMode; // simula nó falhando
    private List<String> wall; // mural de mensagens
    private int lamportClock;

    public Node(String id) {
        this.id = id;
        this.wall = new CopyOnWriteArrayList<>();
        this.lamportClock = 0;
        this.failMode = false;
    }

    // adiciona msg na mural
    public synchronized void addMessage(String msg) {
        lamportClock++;
        wall.add("[" + lamportClock + "] " + msg);
    }

    // getters básicos
    public String getId() {
        return id;
    }

    public List<String> getWall() {
        return wall;
    }

    public int getLamportClock() {
        return lamportClock;
    }

    // simulação de falha
    public boolean isFailMode() {
        return failMode;
    }

    public void setFailMode(boolean failMode) {
        this.failMode = failMode;
    }
}
