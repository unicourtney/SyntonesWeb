package com.blackparty.syntones.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SearchedWordBankDAO;

@Service
public class SearchedWordBankService {
	@Autowired
	private SearchedWordBankDAO searchedDAO;
	
	public boolean updateWord(String word, boolean flag){
		return searchedDAO.updateWord(word, flag);
	}
	public void setStep4(){
		searchedDAO.setStep4();
	}
	public void deleteAllEntities(){
		searchedDAO.deleteAllEntities();
		
	}
	public float step6(long songId){
		return searchedDAO.step6(songId);
	}
	public float step5(ArrayList<String> wb){
		return searchedDAO.step5(wb);
	}
	public List<String> getWords(ArrayList<String> words){
		return searchedDAO.getWords(words);
	}
}
