package com.blackparty.syntones.response;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.UserTransaction;

public class SongListResponse {
	private Message message;
	private UserTransaction userTransaction;
	
	
	public SongListResponse(){}

	
	
	public UserTransaction getUserTransaction() {
		return userTransaction;
	}



	public void setUserTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}



	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	
}
