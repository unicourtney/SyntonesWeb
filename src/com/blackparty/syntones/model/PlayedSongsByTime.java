package com.blackparty.syntones.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "played_songs_by_time_tbl")
public class PlayedSongsByTime {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "track_id")
	private String track_id;

	@Column(name = "midnight")
	private int midnight;
	
	@Column(name = "morning")
	private int morning;
	
	@Column(name = "noon")
	private int noon;
	
	@Column(name = "afternoon")
	private int afternoon;
	
	@Column(name = "evening")
	private int evening;

	
	public PlayedSongsByTime() {
		super();

	}

	public PlayedSongsByTime(String track_id, int midnight, int morning, int noon, int afternoon, int evening) {
		super();
		this.track_id = track_id;
		this.midnight = midnight;
		this.morning = morning;
		this.noon = noon;
		this.afternoon = afternoon;
		this.evening = evening;
	}

	public PlayedSongsByTime(long id, String track_id, int midnight, int morning, int noon, int afternoon,
			int evening) {
		super();
		this.id = id;
		this.track_id = track_id;
		this.midnight = midnight;
		this.morning = morning;
		this.noon = noon;
		this.afternoon = afternoon;
		this.evening = evening;
	}

	public String getTrack_id() {
		return track_id;
	}

	public void setTrack_id(String track_id) {
		this.track_id = track_id;
	}

	public int getMidnight() {
		return midnight;
	}

	public void setMidnight(int midnight) {
		this.midnight = midnight;
	}

	public int getMorning() {
		return morning;
	}

	public void setMorning(int morning) {
		this.morning = morning;
	}

	public int getNoon() {
		return noon;
	}

	public void setNoon(int noon) {
		this.noon = noon;
	}

	public int getAfternoon() {
		return afternoon;
	}

	public void setAfternoon(int afternoon) {
		this.afternoon = afternoon;
	}

	public int getEvening() {
		return evening;
	}

	public void setEvening(int evening) {
		this.evening = evening;
	}
	
	
	
	
}
