package com.blackparty.syntones.DAO;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.SearchedWordBank;


@Repository
@Transactional
public class SearchedWordBankDAO {
	@Autowired
	private SessionFactory sf;

	public void deleteAllEntities() {
		Session session = sf.openSession();
		session.createQuery("Delete from SearchedWordBank where  userId =:userId").executeUpdate();
		session.flush();
		session.close();

	}

	public boolean updateWord(String word, boolean flag) {
		Session session = sf.openSession();
		try {
			Query query = session.createQuery("from SearchedWordBank where word =:word");
			query.setString("word", word);
			SearchedWordBank fetchWordBank = (SearchedWordBank) query.uniqueResult();
			if (fetchWordBank == null) {
				SearchedWordBank swb = new SearchedWordBank();
				swb.setWord(word);
				swb.setTf(1);

				session.save(swb);
				session.flush();
				session.close();
				flag = true;
			} else {
				if (flag) {
					flag = true;
				} else {
					flag = false;
				}
			}
			return flag;
		} catch (Exception e) {
			System.out.println("at SearchedWordBankDAO >>  updateWord(String word) >>" + e);
			return flag;
		}
	}

	public void setStep4() {
		Session session = sf.openSession();
		System.out.println(" here   at SearchedWordBankDAO >>  setStep4() >>");
		try {
			Query query = session.createSQLQuery(
					"Update searched_word_bank a inner join word_bank b on a.word = b.word set a.tfidf =((a.tf/b.max_count)*b.idf)");
			query.executeUpdate();
			session.flush();
			session.close();
		} catch (Exception e) {
			System.out.println("at SearchedWordBankDAO >>  setStep4() >>" + e);
		}
	}

	public float step6(long songId) {
		Session session = sf.openSession();
		Query query = session.createSQLQuery(
				"Select sum(a.tfidf*b.tfidf) as step6 from searched_word_bank a left join song_word_bank b on a.word = b.word where b.song_id =:songId");

		query.setLong("songId", songId);
		Double result = (Double) query.uniqueResult();
		session.flush();
		session.close();
		if (result == null) {
			return 0f;
		} else {
			return result.floatValue();
		}

	}

	public float step5(ArrayList<String> wb) {
		Session session = sf.openSession();
		try {
			Query query = session.createSQLQuery("Select sum(pow(tfidf,2)) from searched_word_bank where word in (:wb)")
					.setParameterList("wb", wb);
			Double result = (Double) query.uniqueResult();
			session.flush();
			session.close();
			if (result == null) {
				return 0f;
			} else {
				return result.floatValue();
			}
		} catch (Exception e) {
			System.out.println("at SearchedWordBankDAO >>  step5() >>" + e);
			return 0f;
		}
	}

	public List<String> getWords(ArrayList<String> words){
		Session session = sf.openSession();
		try {
			Query query = session.createSQLQuery("Select word from searched_word_bank where word in (:words)")
					.setParameterList("words", words);
			List<String> result = (List<String>) query.list();
			session.flush();
			session.close();
			return result;
		}catch(Exception e){
			System.out.println("at SearchedWordBankDAO >>  getWords() >>" + e);
			return null;
		}
	}
}
