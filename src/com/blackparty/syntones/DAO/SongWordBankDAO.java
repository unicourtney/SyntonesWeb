package com.blackparty.syntones.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.IdfModel;
import com.blackparty.syntones.model.SearchResult;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.service.WordBankService;

@Repository
@Transactional
public class SongWordBankDAO {
	@Autowired
	private SessionFactory sf;
	@Autowired
	private WordBankService wbService;

	public void updateWordBank0(List<SongWordBank> words) throws Exception {
		Session session = sf.openSession();

		if (!words.isEmpty()) {
			for (SongWordBank word : words) {
				session.update(word);
			}

			session.flush();
			session.close();
		} else {
			System.out.print("hoholo");
			session.close();
		}
		System.out.print("here @ updateWordBank");
	}

	public List<SongWordBank> fetchAllWordBank() throws Exception {
		Session session = sf.openSession();
		Query query = session.createQuery("from SongWordBank");
		@SuppressWarnings("unchecked")
		List<SongWordBank> words = (List<SongWordBank>) query.list();
		session.close();
		return words;
	}

	public boolean updateWord(String word, long songId, int titleWeight, int artistWeight) {
		Session session = sf.openSession();
		try {
			Query query = session.createQuery("from SongWordBank where songId =:songId and word =:word");
			query.setLong("songId", songId);
			query.setString("word", word);
			SongWordBank fetchWordBank = (SongWordBank) query.uniqueResult();
			if (fetchWordBank == null) {
				SongWordBank swb = new SongWordBank();
				swb.setWord(word);
				swb.setSongId(songId);
				swb.setTf(1 + titleWeight + artistWeight);

				session.save(swb);
				session.flush();
				session.close();

				return wbService.updateWordBank(word);
			} else {
				fetchWordBank.setTf(fetchWordBank.getTf() + 1 + titleWeight);
				session.update(fetchWordBank);
				session.flush();
				session.close();

				return wbService.updateWordBank(word);
			}

		} catch (Exception e) {
			System.out.println("at songwordbankdao >> updateWord >>" + e);
			return false;
		}
	}

	// public boolean updateWordBank(String word) {
	// Session session = sf.openSession();
	// try {
	//
	// Query query = session.createQuery("from SongWordBank where songId
	// =:songId and word =:word");
	// query.setLong("songId", 0);
	// query.setString("word", word);
	// SongWordBank fetchWordBank = (SongWordBank) query.uniqueResult();
	// if (fetchWordBank == null) {
	// SongWordBank swb = new SongWordBank();
	// swb.setWord(word);
	// swb.setSongId(0);
	// swb.setTf(1);
	// swb.setMaxCount(1);
	//
	// session.save(swb);
	// session.flush();
	// session.close();
	//
	// return true;
	// } else {
	// fetchWordBank.setTf(fetchWordBank.getTf() + 1);
	// fetchWordBank.setMaxCount(fetchWordBank.getMaxCount() + 1);
	// session.update(fetchWordBank);
	// session.flush();
	// session.close();
	//
	// return true;
	// }
	// } catch (Exception e) {
	// System.out.println("at songwordbankdao >> updateWordBank>>" + e);
	// return false;
	// }
	// }

	@SuppressWarnings("unchecked")
	public List<SongWordBank> fetchWordBankbySongId(long songId) {
		Session session = sf.openSession();
		try {
			Query query = session.createQuery("from SongWordBank where songId =:songId");
			query.setLong("songId", songId);
			List<SongWordBank> words = (List<SongWordBank>) query.list();
			session.close();
			return words;
		} catch (Exception e) {
			System.out.println("at songwordbankdao >> fetchWordBankbySongId>> " + e);
			return null;
		}
	}

	public List<SongWordBank> getMaxWB() {
		Session session = sf.openSession();
		try {
			Query query = session.createQuery("from SongWordBank where songId =:songId");
			query.setLong("songId", 0);
			@SuppressWarnings("unchecked")
			List<SongWordBank> words = (List<SongWordBank>) query.list();
			session.close();
			return words;
		} catch (Exception e) {
			System.out.println("at songwordbankdao getMaxWB() >> " + e);
			return null;
		}
	}

	public boolean checkSongId(long songId) {
		Session session = sf.openSession();
		Query q = session.createQuery("from SongWordBank where songId =:songId");
		q.setLong("songId", songId);
		SongWordBank fetched = (SongWordBank) q.setMaxResults(1).uniqueResult();

		if (fetched == null) {
			session.flush();
			session.close();
			return true;
		}
		return false;
	}

	// public void updateStep3(List<SongWordBank> swbList, long songId) {
	// Session session = sf.openSession();
	// Query q = session.createQuery("from SongWordBank where songId =:songId");
	// q.setLong("songId", songId);
	// List<SongWordBank> swList = q.list();
	// session.flush();
	//
	// for (SongWordBank sw : swList) {
	// for (SongWordBank word : swbList) {
	// if (sw.getWord().equalsIgnoreCase(word.getWord())) {
	// sw.setTfidf((float) sw.getTf() * (float) word.getIdf());
	// session.update(sw);
	// break;
	// }
	// }
	// }
	// session.flush();
	// session.close();
	// }

	@SuppressWarnings("unchecked")
	public List<Long> getDistinctId(ArrayList<String> words) {
		Session session = sf.openSession();
		try {
			Query q = session.createQuery("select Distinct songId from SongWordBank where word in(:words)")
					.setParameterList("words", words);
			List<Long> songIds = (List<Long>) q.list();
			session.flush();
			session.close();

			return songIds;
		} catch (Exception e) {
			System.out.println("@ SWBDAO >> getDistinctId >> e " + e);
			return null;
		}
	}
	public List<Long> getDistinctIdBySearchedStrings(ArrayList<String> words) {
		Session session = sf.openSession();
		try {
			Query q = session
					.createQuery(
							"select Distinct songId from SongWordBank where word in(:words) group by songId having count(Distinct word)=:size")
					.setParameterList("words", words).setInteger("size", words.size());
			List<Long> songIds = (List<Long>) q.list();
			session.flush();
			session.close();

			return songIds;
		} catch (Exception e) {
			System.out.println("@ SWBDAO >> getDistinctIdBySearchedStrings >> e " + e);
			return null;
		}
	}
	public List<Long> getDistinctForAnd(ArrayList<SearchResult> resultT) {
		Session session = sf.openSession();
		try{
		ArrayList<Long> songIds = new ArrayList<Long>();
		for (SearchResult rt : resultT) {
			Query q = session
					.createQuery("select Distinct songId from SongWordBank where songId=:songId and word in(:words)")
					.setParameterList("words", wbService.getWords()).setLong("songId", rt.getSongId());
			songIds.add((Long) q.setMaxResults(1).uniqueResult());
			session.flush();
		}
		session.close();

		return songIds;
		}catch(Exception e){
			System.out.println("@ SWBDAO >> getDistinctForAnd >> e " + e);
			return null;
		}
	}

	public List<Long> getDistinctForOr(ArrayList<IdfModel> resultT) {
		Session session = sf.openSession();
		Query q = session.createQuery("select Distinct songId from SongWordBank where songId != 0");
		List<Long> songIds = q.list();
		session.flush();
		session.close();
		songIds.removeAll(resultT);
		return songIds;
	}

	public void setStep3() {
		Session session = sf.openSession();
		Query q = session.createSQLQuery(
				"Update song_word_bank a Inner join word_bank b on a.word = b.word set a.tfidf = a.tf*b.idf where a.tfidf = 0");
		q.executeUpdate();
		session.flush();
		session.close();
	}

	public float getStep5(long songId) {
		Session session = sf.openSession();
		Query q = session.createSQLQuery("Select sum(Pow(tfidf,2)) from song_word_bank where song_id =:songId ");
		q.setLong("songId", songId);
		double step5 = (double) q.uniqueResult();
		// System.out.println("step5 >> "+step5);
		session.flush();
		session.close();

		return (float) step5;
	}

	public List<SongWordBank> checkWord(List<String> words, long songId) {
		Session session = sf.openSession();
		try{
		Query q = session.createQuery("from SongWordBank where word in(:words) and songId =:songId1")
				.setParameterList("words", words).setLong("songId1", songId);
		@SuppressWarnings("unchecked")
		List<SongWordBank> swords = (List<SongWordBank>) q.list();
		session.flush();
		session.close();

		return swords;
		}catch(Exception e){
			System.out.println("@ SWBDAO >> checkWord >> e "+e);
			return null;
		}
	}

}