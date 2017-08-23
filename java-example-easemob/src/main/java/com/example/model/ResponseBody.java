package com.example.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBody<D> {

    @SerializedName("application")
    private String application;
    @SerializedName("application_name")
    private String applicationName;
    @SerializedName("organization")
    private String organization;

    @SerializedName("action")
    private String action;
    @SerializedName("path")
    private String path;
    @SerializedName("uri")
    private String uri;
    @SerializedName("timestamp")
    private Long timestamp;
    @SerializedName("duration")
    private Long duration;

    @SerializedName("entities")
    private List<Entity> entities;
    @SerializedName("data")
    private D data;

    public static class Entity {
        @SerializedName("uuid")
        private String uuid;
        @SerializedName("type")
        private String type;
        @SerializedName("created")
        private Long created;
        @SerializedName("modified")
        private Long modified;
        @SerializedName("username")
        private String username;
        @SerializedName("activated")
        private Boolean activated;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Long getCreated() {
            return created;
        }

        public void setCreated(Long created) {
            this.created = created;
        }

        public Long getModified() {
            return modified;
        }

        public void setModified(Long modified) {
            this.modified = modified;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Boolean getActivated() {
            return activated;
        }

        public void setActivated(Boolean activated) {
            this.activated = activated;
        }
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
