package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "searched_result_tbl")
public class SearchResult {
	@Id
	@Column(name = "result_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long resultId;

	@Column(name = "search_id")
	private long searchId;

	@Column(name = "song_id")
	private long songId;

	@Column(name = "degree")
	private float degree;

	public SearchResult() {

	}

	public long getResultId() {
		return resultId;
	}

	public void setResultId(long resultId) {
		this.resultId = resultId;
	}

	public long getSearchId() {
		return searchId;
	}

	public void setSearchId(long searchId) {
		this.searchId = searchId;
	}

	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId) {
		this.songId = songId;
	}

	public float getDegree() {
		return degree;
	}

	public void setDegree(float degree) {
		this.degree = degree;
	}

}
