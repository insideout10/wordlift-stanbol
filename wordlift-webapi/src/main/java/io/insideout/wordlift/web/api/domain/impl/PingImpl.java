package io.insideout.wordlift.web.api.domain.impl;

public class PingImpl {

    private String pong;

    public PingImpl() {}

    public PingImpl(String pong) {
        this.pong = pong;
    }

    public String getPong() {
        return pong;
    }

    public void setPong(String pong) {
        this.pong = pong;
    }

}
