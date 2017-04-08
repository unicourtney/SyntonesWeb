package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="word_bank")
public class WordBank {
	@Id
	@Column(name="word_id")
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "artist_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	private long wordId;
	
	@Column(name="max_count")
	private int maxCount;
	
	@Column(name="idf")
	private float idf;
	
	@Column(name="word")
	private String word;
	
	public  WordBank(){
		
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public float getIdf() {
		return idf;
	}

	public void setIdf(float idf) {
		this.idf = idf;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	
}
