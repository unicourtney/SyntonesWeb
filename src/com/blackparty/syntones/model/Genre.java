package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="genre_tbl")
public class Genre {

	@Id
	@Column(name="id")
	private long id;
	
	@Column(name="genre")
	private String genre;
	
	
	public Genre(){
		
		
	}


	public Genre(long id, String genre) {
		super();
		this.id = id;
		this.genre = genre;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getGenre() {
		return genre;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	
	
	
}
