package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Genre;
import com.blackparty.syntones.model.Message;

public class GenreResponse {
	private Message message;

	private List<Genre> genre;

	public GenreResponse() {
		super();

	}

	public GenreResponse(Message message, List<Genre> genre) {
		super();
		this.message = message;
		this.genre = genre;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<Genre> getGenre() {
		return genre;
	}

	public void setGenre(List<Genre> genre) {
		this.genre = genre;
	}

}
