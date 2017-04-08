package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.DataSetWord;

@Repository
@Transactional
public class DataSetWordBankDAO {
	@Autowired
	private SessionFactory sf;
	public boolean save(List<String> word)throws Exception{
		for(String s:word){
			DataSetWord dsw = new DataSetWord();
			dsw.setWord(s);
			
			Session session = sf.openSession();
			session.save(dsw);
			session.flush();
			session.close();
		}
		return true;
	}
	
	public List<DataSetWord> getAll()throws Exception{
		Session session = sf.openSession();
		Query query = session.createQuery("from DataSetWordBank");
		
		List<DataSetWord> wordList = query.list();
		session.clear();
		session.flush();
		session.close();
		
		return wordList;
		
	}
}