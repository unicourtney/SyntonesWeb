package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.User;


public class LoginResponse {
	
	private Message message;
	private List<Playlist> recentPlaylistsPlayed;
	
	public LoginResponse(){}

	
	public LoginResponse(User user,Message message, List<Playlist> recentPlaylistsPlayed){
		this.message = message;
		this.recentPlaylistsPlayed = recentPlaylistsPlayed;
		
	}
	
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<Playlist> getRecentPlaylistsPlayed() {
		return recentPlaylistsPlayed;
	}

	public void setRecentPlaylistsPlayed(List<Playlist> recentPlaylistsPlayed) {
		this.recentPlaylistsPlayed = recentPlaylistsPlayed;
	}
	
		
}
