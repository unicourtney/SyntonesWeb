package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dataset_conditional_probability_tbl")
public class DataSetConditionalProbability {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(name="mood_id")
	private int moodId;
	@Column(name="word")
	private String word;
	@Column(name="conditional_probability")
	private double conditionalProbability;
	
	public DataSetConditionalProbability(){
		
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public int getMoodId() {
		return moodId;
	}

	public void setMoodId(int moodId) {
		this.moodId = moodId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public double getConditionalProbability() {
		return conditionalProbability;
	}

	public void setConditionalProbability(double conditionalProbability) {
		this.conditionalProbability = conditionalProbability;
	}

	@Override
	public String toString() {
		return "DataSetConditionalProbability [moodId=" + moodId + ", word=" + word + ", conditionalProbability="
				+ conditionalProbability + "]";
	}
	
}
	