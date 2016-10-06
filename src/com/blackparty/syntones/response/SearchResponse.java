package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Song;
public class SearchResponse {
    private Message message;
    private List<Song> songs;
    private List<Artist> artists;

    public SearchResponse() {
    }

    public SearchResponse(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

}
