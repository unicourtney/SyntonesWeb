package com.blackparty.syntones.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "played_songs_tbl")
public class PlayedSongs {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "session_id")
	private String session_id;

	@Column(name = "track_id")
	private String track_id;

	@Column(name = "date")
	private Timestamp date;

	@Column(name = "user_id")
	private long user_id;

	@Column(name = "part_of_day")
	private String part_of_day;

	public PlayedSongs() {
		super();
	}

	public PlayedSongs( String session_id, String track_id, Timestamp date, long user_id, String part_of_day) {
		super();
	
		this.session_id = session_id;
		this.track_id = track_id;
		this.date = date;
		this.user_id = user_id;
		this.part_of_day = part_of_day;
	}

	public PlayedSongs(String session_id, String track_id, Timestamp date, long user_id) {
		super();
		this.id = id;
		this.session_id = session_id;
		this.track_id = track_id;
		this.date = date;
		this.user_id = user_id;
	}

	public PlayedSongs(String session_id, String track_id, Timestamp date) {
		super();
		this.session_id = session_id;
		this.track_id = track_id;
		this.date = date;
	}

	public PlayedSongs(long id, String session_id, String track_id, Timestamp date) {
		super();
		this.id = id;
		this.session_id = session_id;
		this.track_id = track_id;
		this.date = date;
	}

	public PlayedSongs(String session_id, String track_id) {
		super();
		this.session_id = session_id;
		this.track_id = track_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getTrack_id() {
		return track_id;
	}

	public void setTrack_id(String track_id) {
		this.track_id = track_id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getPart_of_day() {
		return part_of_day;
	}

	public void setPart_of_day(String part_of_day) {
		this.part_of_day = part_of_day;
	}

}
