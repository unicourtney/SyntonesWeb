package com.blackparty.syntones.response;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;

public class PlaylistResponse {
	private Message message;
	private Playlist playlist;
	
	
	public PlaylistResponse(){}
	public PlaylistResponse(Playlist playlist, Message message){
		this.playlist = playlist;
		this.message = message;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public Playlist getPlaylist() {
		return playlist;
	}
	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
	
}
