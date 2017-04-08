package com.blackparty.syntones.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SongWordBankDAO;
import com.blackparty.syntones.model.IdfModel;
import com.blackparty.syntones.model.SearchResult;
import com.blackparty.syntones.model.SongWordBank;

@Service
public class SongWordBankService {
	@Autowired
	private SongWordBankDAO swbDao;

	public void updateWordBank1(List<SongWordBank> words) throws Exception {
		swbDao.updateWordBank0(words);
	}

	public boolean updateWord(String word, long songId, int titleWeight, int artistWeight) throws Exception {
		return swbDao.updateWord(word, songId, titleWeight, artistWeight);
	}

	public List<SongWordBank> fetchAllWordBank() throws Exception {
		return swbDao.fetchAllWordBank();
	}

	public List<SongWordBank> fetchWBbySongId(long songId) throws Exception {
		return swbDao.fetchWordBankbySongId(songId);
	}

	public List<SongWordBank> getMaxWB() {
		return swbDao.getMaxWB();
	}

	public boolean checkSongId(long songId) {
		return swbDao.checkSongId(songId);
	}

//	public void updateStep3(List<SongWordBank> swbList, long songId) {
//		swbDao.updateStep3(swbList, songId);
//	}

	public List<Long> getDistinctId(ArrayList<String> words) {
		return swbDao.getDistinctId(words);
	}

	public void setStep3() {
		swbDao.setStep3();
	}

	public float getStep5(long songId) {
		return swbDao.getStep5(songId);
	}
	public List<Long> getDistinctForAnd(ArrayList<SearchResult> resultT){
		return swbDao.getDistinctForAnd(resultT);
	}
	public List<Long> getDistinctForOr(ArrayList<IdfModel> resultT) {
		return swbDao.getDistinctForOr(resultT);
	}
	public List<Long> getDistinctIdBySearchedStrings(ArrayList<String> words) {
		return swbDao.getDistinctIdBySearchedStrings(words);
	}

	public List<SongWordBank> checkWord(ArrayList<String> wb, Long songId) {
		return swbDao.checkWord(wb, songId);
	}
}
