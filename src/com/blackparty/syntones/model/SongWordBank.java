package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="song_word_bank")
public class SongWordBank {
	
	@Id
	@Column(name="word_id")
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "artist_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	private long wordId;
	
	@Column(name="song_id")
	private long songId;
	
	@Column(name="word")
	private String word;
	
	@Column(name="tf")
	private float tf;
	
	@Column(name="tfidf")
	private float tfidf;

	public SongWordBank(){
		
	}
	
	
	public long getWordId() {
		return wordId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public long getSongId() {
		return songId;
	}

	public void setSongId(long songId2) {
		this.songId = songId2;
	}

	public float getTf() {
		return tf;
	}

	public void setTf(float tf) {
		this.tf = tf;
	}

	public float getTfidf() {
		return tfidf;
	}

	public void setTfidf(float tfidf) {
		this.tfidf = tfidf;
	}
	
	
}
