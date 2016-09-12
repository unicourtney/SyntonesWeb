package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name="artist_tbl")
public class Artist {

	@Id
	@Column(name="artist_id")
	@TableGenerator(name = "table_gen", table = "sequence_table", pkColumnName = "seq_name", valueColumnName = "seq_count", pkColumnValue = "artist_seq")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen")
	private long artistId;
	
	
	@Column(name="artist_name")
	private String artistName;
	
	public Artist(){
		
	}
	

	public Artist(String artistName){
		this.artistName = artistName;
	}
	public long getArtistId() {
		return artistId;
	}

	public String getArtistName() {
		return artistName;
	}

	
	
}
