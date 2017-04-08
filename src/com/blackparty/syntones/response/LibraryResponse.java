package com.blackparty.syntones.response;

import java.util.List;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.PlaylistSong;
import com.blackparty.syntones.model.User;

public class LibraryResponse {

	private Message message;
	private List<Playlist> recentlyPlayedPlaylists;
	private List<PlaylistSong> playlistSong;
	private User user;
	private List<Long> notExistingSongs;

	public LibraryResponse() {
	}

	public LibraryResponse(Message message, List<Playlist> recentlyPlayedPlaylists, List<PlaylistSong> playlistSong,
			User user, List<Long> notExistingSongs) {
		super();
		this.message = message;
		this.recentlyPlayedPlaylists = recentlyPlayedPlaylists;
		this.playlistSong = playlistSong;
		this.user = user;
		this.notExistingSongs = notExistingSongs;
	}

	public LibraryResponse(Message message, List<Playlist> recentlyPlayedPlaylists, List<PlaylistSong> playlistSong,
			User user) {
		super();
		this.message = message;
		this.recentlyPlayedPlaylists = recentlyPlayedPlaylists;
		this.playlistSong = playlistSong;
		this.user = user;
	}

	public LibraryResponse(User user, Message message, List<Playlist> recentlyPlayedPlaylists) {
		this.message = message;
		this.recentlyPlayedPlaylists = recentlyPlayedPlaylists;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<Playlist> getRecentlyPlayedPlaylists() {
		return recentlyPlayedPlaylists;
	}

	public void setRecentlyPlayedPlaylists(List<Playlist> recentPlaylistsPlayed) {
		this.recentlyPlayedPlaylists = recentPlaylistsPlayed;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<PlaylistSong> getPlaylistSong() {
		return playlistSong;
	}

	public void setPlaylistSong(List<PlaylistSong> playlistSong) {
		this.playlistSong = playlistSong;
	}

	public List<Long> getNotExistingSongs() {
		return notExistingSongs;
	}

	public void setNotExistingSongs(List<Long> notExistingSongs) {
		this.notExistingSongs = notExistingSongs;
	}

}
