package com.blackparty.syntones.model;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table(name = "temporary_db_tbl")
public class TemporaryDB {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "session_id")
	private String session_id;

	@Column(name = "song_id")
	private long song_id;

	@Column(name = "date")
	private Timestamp date;

	@Column(name = "user_id")
	private long user_id;

	@Column(name = "part_of_day")
	private String part_of_day;

	public TemporaryDB() {
		super();
	}

	public TemporaryDB( String session_id, long song_id, Timestamp date, long user_id, String part_of_day) {
		super();

		this.session_id = session_id;
		this.song_id = song_id;
		this.date = date;
		this.user_id = user_id;
		this.part_of_day = part_of_day;
	}

	public TemporaryDB(String session_id, long song_id, Timestamp date, long user_id) {
		super();
		this.session_id = session_id;
		this.song_id = song_id;
		this.date = date;
		this.user_id = user_id;
	}

	public TemporaryDB(String session_id, long song_id, Timestamp date) {
		super();
		this.session_id = session_id;
		this.song_id = song_id;
		this.date = date;
	}

	public TemporaryDB(Long id, String session_id, long song_id) {
		super();
		this.id = id;
		this.session_id = session_id;
		this.song_id = song_id;
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

	public long getSong_id() {
		return song_id;
	}

	public void setSong_id(long song_id) {
		this.song_id = song_id;
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
