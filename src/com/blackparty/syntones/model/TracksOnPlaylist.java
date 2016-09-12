package com.blackparty.syntones.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="tracks_on_playlist_tbl")
public class TracksOnPlaylist {
	
	
	@Column(name="")
	private Playlist playlist;
	private Song song;
	
	
}
