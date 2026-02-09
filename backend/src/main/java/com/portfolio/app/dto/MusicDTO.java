package com.portfolio.app.dto;

import java.time.LocalDateTime;

public class MusicDTO {
    private String songName;
    private String artist;
    private String albumArt; // URL or base64
    private String status; // "LISTENING" or "LAST_PLAYED"
    private LocalDateTime lastUpdated;

    public MusicDTO() {
    }

    public MusicDTO(String songName, String artist, String albumArt, String status, LocalDateTime lastUpdated) {
        this.songName = songName;
        this.artist = artist;
        this.albumArt = albumArt;
        this.status = status;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
