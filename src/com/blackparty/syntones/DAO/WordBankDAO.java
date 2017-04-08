package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.WordBank;

@Repository
@Transactional
public class WordBankDAO {
	@Autowired
	private SessionFactory sf;

	public boolean updateWordBank(String word) {
		Session session = sf.openSession();
		try {

			Query query = session.createQuery("from WordBank where word =:word");
			query.setString("word", word);
			WordBank fetchWordBank = (WordBank) query.uniqueResult();
			if (fetchWordBank == null) {
				WordBank swb = new WordBank();
				swb.setWord(word);
				swb.setMaxCount(1);

				session.save(swb);
				session.flush();
				session.close();

				return true;
			} else {
				fetchWordBank.setMaxCount(fetchWordBank.getMaxCount() + 1);
				session.update(fetchWordBank);
				session.flush();
				session.close();

				return true;
			}
		} catch (Exception e) {
			System.out.println("at wordbankdao >> updateWordBank>>" + e);
			return false;
		}
	}

	public List<WordBank> getMaxWB() {
		Session session = sf.openSession();
		try {
			Query query = session.createQuery("from WordBank");
			@SuppressWarnings("unchecked")
			List<WordBank> words = (List<WordBank>) query.list();
			session.close();
			return words;
		} catch (Exception e) {
			System.out.println("at wordbankdao getMaxWB() >> " + e);
			return null;
		}
	}

	public void updateIDF(List<WordBank> words) throws Exception {
		Session session = sf.openSession();

		if (!words.isEmpty()) {
			for (WordBank word : words) {
				session.update(word);
			}

			session.flush();
			session.close();
		} else {
			System.out.print("hoholo");
			session.close();
		}
		System.out.print("here @ updateIDF");
	}

	public List<String> getWords() throws Exception {
		Session session = sf.openSession();
		try {
			Query query = session.createQuery("select word from WordBank");
			List<String> words = query.list();
			session.flush();
			session.close();
			
			return words;
		} catch (Exception e) {
			System.out.println("at wordbankdao getWords() >> " + e);
			return null;
		}
	}
	
	public List<String> getWordsFromWildcard(String sword){
		Session session = sf.openSession();
		try{
			Query query = session.createQuery("Select word from WordBank where word like :sword");
			query.setString("sword", sword);
			List<String> words = query.list();
			session.flush();
			session.close();
			return words;
		}catch(Exception e){
			System.out.println("@ wordbank getWordsFromWildCard() >>"+e);
			return null;
		}
		
	}

	public void processIDF(long songCount) throws Exception {
		Session session = sf.openSession();
		Query query = session.createSQLQuery("Update word_bank SET idf = log2(:songCount/max_count)").setParameter("songCount", songCount);
		query.executeUpdate();
		session.flush();
		session.close();
		System.out.print("here @ processIDF");
	}
}
