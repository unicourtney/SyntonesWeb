package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.Song;

public class PlaylistSongsResponse {
	private List<Song> songsOnPlaylist;
	private Message message;
	
	public PlaylistSongsResponse(){}
	
	


	public List<Song> getSongsOnPlaylist() {
		return songsOnPlaylist;
	}




	public void setSongsOnPlaylist(List<Song> songsOnPlaylist) {
		this.songsOnPlaylist = songsOnPlaylist;
	}




	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	
}
