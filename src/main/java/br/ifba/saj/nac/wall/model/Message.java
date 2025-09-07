package br.ifba.saj.nac.wall.model;

import java.io.Serializable;

// Representa uma mensagem do mural
public class Message implements Serializable {
    private String id;
    private String user;
    private String text;
    private long timestamp;
    private int lamport;
    private String origin;

    public Message(String id, String user, String text, long timestamp, int lamport, String origin) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.timestamp = timestamp;
        this.lamport = lamport;
        this.origin = origin;
    }

    // getters e setters
    public String getId() { return id; }
    public String getUser() { return user; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
    public int getLamport() { return lamport; }
    public String getOrigin() { return origin; }
}