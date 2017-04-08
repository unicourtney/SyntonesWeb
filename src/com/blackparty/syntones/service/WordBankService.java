package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.WordBankDAO;
import com.blackparty.syntones.model.WordBank;

@Service
public class WordBankService {
	@Autowired
	private WordBankDAO wbDao;
	
	public boolean updateWordBank(String word) {
		return wbDao.updateWordBank(word);
	}
	
	public List<WordBank> getMaxWB() {
		return wbDao.getMaxWB();
	}
	public void updateIDF(List<WordBank> words) throws Exception {
		wbDao.updateIDF(words);
	}
	public List<String> getWords() throws Exception {
		return wbDao.getWords();
	}	
	public List<String> getWordsFromWildcard(String sword){
		return wbDao.getWordsFromWildcard(sword);
	}

	public void processIDF(long songCount) throws Exception {
		wbDao.processIDF(songCount);
	}
}
