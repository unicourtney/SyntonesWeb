package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "one_item_set_count_tbl")
public class OneItemSetCount {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "track_id")
	private String track_id;

	@Column(name = "count")
	private int count;
	
	@Column(name = "support")
	private double support;

	public OneItemSetCount() {
		super();
	}

	public OneItemSetCount(String track_id, int count, double support) {
		super();
		this.track_id = track_id;
		this.count = count;
		this.support = support;
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



	public double getSupport() {
		return support;
	}



	public void setSupport(double support) {
		this.support = support;
	}
	
	

}
