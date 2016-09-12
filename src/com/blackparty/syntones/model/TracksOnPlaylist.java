package com.blackparty.syntones.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="tracks_on_playlist_tbl")
public class TracksOnPlaylist {
	
	
	@Id
	private long id;
	
	@Transient
	private Playlist playlist;
	@Transient
	private Song song;
	
}
