package com.blackparty.syntones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="searched_word_tbl")
public class SearchedWord {
	@Id
	@Column(name="search_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long searchId;
	
	@Column(name="search_word")
	private String searchWord;

	public SearchedWord(){
		
	}

	public long getSearchId() {
		return searchId;
	}

	public void setSearchId(long searchId) {
		this.searchId = searchId;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	
	
}
