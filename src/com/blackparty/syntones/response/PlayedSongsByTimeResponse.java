package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.PlayedSongsByTime;

public class PlayedSongsByTimeResponse {
	private Message message;
	private List<PlayedSongsByTime> playedSongsByTimeList;

	public PlayedSongsByTimeResponse() {
		super();

	}

	public PlayedSongsByTimeResponse(Message message, List<PlayedSongsByTime> playedSongsByTimeList) {
		super();
		this.message = message;
		this.playedSongsByTimeList = playedSongsByTimeList;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<PlayedSongsByTime> getPlayedSongsByTimeList() {
		return playedSongsByTimeList;
	}

	public void setPlayedSongsByTimeList(List<PlayedSongsByTime> playedSongsByTimeList) {
		this.playedSongsByTimeList = playedSongsByTimeList;
	}

}
