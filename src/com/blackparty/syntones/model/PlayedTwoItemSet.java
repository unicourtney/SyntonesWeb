package com.blackparty.syntones.model;

public class PlayedTwoItemSet {

	private String combination;
	private String user_id;

	public PlayedTwoItemSet() {
		super();
	}

	public PlayedTwoItemSet(String combination, String user_id) {
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
