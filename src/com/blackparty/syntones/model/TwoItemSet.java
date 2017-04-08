package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "two_item_set_tbl")
public class TwoItemSet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "track_id")
	private String track_id;

	@Column(name = "count")
	private int count;

	@Column(name = "support")
	private double support;
	
	@Column(name = "confidence")
	private double confidence;

	@Column(name = "recom_song")
	private long recom_song;

	public TwoItemSet() {
		super();
	}

	public TwoItemSet(String track_id, int count, double support) {
		super();
		this.track_id = track_id;
		this.count = count;
		this.support = support;
	}

	public TwoItemSet(String track_id, long recom_song, int count, double support, double confidence) {
		super();
		this.track_id = track_id;
		this.count = count;
		this.confidence = confidence;
		this.support = support;
		this.recom_song = recom_song;
	}

	public String getTrack_id() {
		return track_id;
	}

	public void setTrack_id(String track_id) {
		this.track_id = track_id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public long getRecom_song() {
		return recom_song;
	}

	public void setRecom_song(long recom_song) {
		this.recom_song = recom_song;
	}

	public double getSupport() {
		return support;
	}

	public void setSupport(double support) {
		this.support = support;
	}
	
	

}
