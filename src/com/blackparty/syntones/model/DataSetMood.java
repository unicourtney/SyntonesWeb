package com.blackparty.syntones.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="dataset_mood_tbl")
public class DataSetMood {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="mood")
	private String moodName;
	@Column(name="prior")
	private double prior;
	
	@Transient
	private List<DataSetConditionalProbability> conditionalProbabilities;

	public DataSetMood(){
		
		
	}



	public String getMoodName() {
		return moodName;
	}

	public void setMoodName(String moodName) {
		this.moodName = moodName;
	}





	public double getPrior() {
		return prior;
	}





	public void setPrior(double prior) {
		this.prior = prior;
	}




	public List<DataSetConditionalProbability> getConditionalProbabilities() {
		return conditionalProbabilities;
	}



	public void setConditionalProbabilities(List<DataSetConditionalProbability> conditionalProbabilities) {
		this.conditionalProbabilities = conditionalProbabilities;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	    public String toString() {
	        return "Mood{" + "moodName=" + getMoodName() + ", prior=" + getPrior() + '}';
	    }

}
