package com.blackparty.syntones.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;


public class Playlist {

	@Id
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "play_list_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	@Column(name="playlist_id")
	private Long playListId;
	
	
	@ManyToOne(optional = false)
	@JoinColumn(referencedColumnName = "user_id")
	private User user;

	
	private TracksOnPlaylist tracksOnPlaylist;
	
	@Transient
	private List<Song> songList;
	@Transient
	private String[] songIdList;

	public Playlist() {
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Song> getSongList() {
		return songList;
	}

	public void setSongList(List<Song> songList) {
		this.songList = songList;
	}

	public String[] getSongIdList() {
		return songIdList;
	}

	public void setSongIdList(String[] songIdList) {
		this.songIdList = songIdList;
	}

}
