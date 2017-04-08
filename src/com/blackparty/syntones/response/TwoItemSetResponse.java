package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.TwoItemSet;

public class TwoItemSetResponse {

	private Message message;
	private List<Song> songList;

	public TwoItemSetResponse() {
		super();
	}

	public TwoItemSetResponse(Message message, List<Song> songList) {
		super();
		this.message = message;
		this.songList = songList;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<Song> getSongList() {
		return songList;
	}

	public void setSongList(List<Song> songList) {
		this.songList = songList;
	}

}
