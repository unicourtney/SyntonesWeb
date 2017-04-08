package com.blackparty.syntones.model;

public class SearchModel {
	private long sid;
	private long aid;
	private float degree;
	
	public SearchModel(){
		
	}

	public long getSongId() {
		return sid;
	}
	
	public long getArtistId(){
		return aid;
	}
	
	public void setArtistId(long aid){
		this.aid = aid;
	}

	public void setSongId(long sid) {
		this.sid = sid;
	}

	public float getDegree() {
		return degree;
	}

	public void setDegree(float degree) {
		this.degree = degree;
	}


	

	
}
