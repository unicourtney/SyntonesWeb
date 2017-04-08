package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dataset_song_tbl")
public class DataSetSong {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="number")
	private int number;
	@Column(name="artist_name")
	private String artistName;
	@Column(name="title")
	private String title;
	@Column(name="album")
	private String album;
	@Column(name="mood")
	private String mood;
	@Column(name="lyrics")
	private String lyrics;
	@Column(name="lyrics_cleaned")
	private String lyricsCleaned;
	
	
	public DataSetSong(){
		
		
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}


	public String getArtistName() {
		return artistName;
	}


	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getAlbum() {
		return album;
	}


	public void setAlbum(String album) {
		this.album = album;
	}


	public String getMood() {
		return mood;
	}


	public void setMood(String mood) {
		this.mood = mood;
	}


	public String getLyrics() {
		return lyrics;
	}


	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}


	public String getLyricsCleaned() {
		return lyricsCleaned;
	}


	public void setLyricsCleaned(String lyricsCleaned) {
		this.lyricsCleaned = lyricsCleaned;
	}
	
	
	
	
}
