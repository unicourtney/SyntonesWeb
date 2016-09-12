package com.blackparty.syntones.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

@Entity
@Table(name="playlist_tbl")
public class Playlist {

	@Id
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "play_list_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	@Column(name="playlist_id")
	private Long playlistId;
	
	@Column(name="playlist_name")
	private String playlistName;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="playlist_song",joinColumns=@JoinColumn(name="playlist_id"),inverseJoinColumns=@JoinColumn(name="song_id"))
	private List<Song> songs;
	
	@ManyToOne(optional = false)
	@JoinColumn(referencedColumnName = "user_id")
	private User user;
	
	@Transient
	private String[] songIdList;

	public Playlist(){
		
	}
	
	
	
	public Playlist(Long playlistId, String playlistName, List<Song> songs, User user, String[] songIdList) {
		super();
		this.playlistId = playlistId;
		this.playlistName = playlistName;
		this.songs = songs;
		this.user = user;
		this.songIdList = songIdList;
	}



	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String[] getSongIdList() {
		return songIdList;
	}

	public void setSongIdList(String[] songIdList) {
		this.songIdList = songIdList;
	}

	public Long getPlaylistId() {
		return playlistId;
	}

	public void setPlayListId(Long playListId) {
		this.playlistId = playListId;
	}

	public String getPlaylistName() {
		return playlistName;
	}

	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}


	

	public void setPlaylistId(Long playlistId) {
		this.playlistId = playlistId;
	}



	public List<Song> getSongs() {
		return songs;
	}



	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}



	@Override
	public String toString() {
		return "Playlist [playlistId=" + playlistId + ", playlistName=" + playlistName + ", songs=" + songs + "]";
	}

	
	

}
