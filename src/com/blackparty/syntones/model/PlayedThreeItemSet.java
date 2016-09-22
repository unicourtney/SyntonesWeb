package com.blackparty.syntones.model;

public class PlayedThreeItemSet {
	private String combination;
	private String user_id;

	public PlayedThreeItemSet() {
		super();
	}

	public PlayedThreeItemSet(String combination, String user_id) {
		super();
		this.combination = combination;
		this.user_id = user_id;
	}

	public String getCombination() {
		return combination;
	}

	public void setCombination(String combination) {
		this.combination = combination;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

}
