package br.com.spotify.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Album {

    private String id;

    private String name;

    @JsonProperty("release_date")
    private String releaseDate;

    public Album() {
    }

    public Album(String id, String name, String releaseDate) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
