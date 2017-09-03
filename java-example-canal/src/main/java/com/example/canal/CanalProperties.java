package com.example.canal;

public class CanalProperties {

    private String host = "localhost";
    private Integer port = 11111;
    private String destination = "example";
    private String username = "";
    private String password = "";
    private Long reconnectMillis = 6000L;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getReconnectMillis() {
        return reconnectMillis;
    }

    public void setReconnectMillis(Long reconnectMillis) {
        this.reconnectMillis = reconnectMillis;
    }
}
