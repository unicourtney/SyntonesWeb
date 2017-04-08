package com.blackparty.syntones.DAO;

import java.util.List;



import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.DataSetMood;
import com.blackparty.syntones.service.DataSetConditionalProbabilityService;


@Repository
@Transactional
public class DataSetMoodDAO {
	@Autowired
	private SessionFactory sf;
	@Autowired
	private DataSetConditionalProbabilityService dsConProService;
	
	
	public List<DataSetMood> getAllMood() throws Exception {
		System.out.println("fetching all dataset mood on the database");
		Session session = sf.openSession();
		Query query = session.createQuery("from DataSetMood");
		List<DataSetMood> moodList = query.list();
		session.flush();
		session.close();
		
		return moodList;
	}
	
	public boolean updateBatchAllMood(List<DataSetMood> moodList)throws Exception{
		System.out.print("Attempting to update mood...");
		int counter = 1;
		StatelessSession session = sf.openStatelessSession();
		Transaction trans = session.beginTransaction();
		for(DataSetMood s:moodList){
			session.update(s);
			System.out.println(counter++);
		}
		trans.commit();
		session.close();
		
		dsConProService.save(moodList);
		
		System.out.println("Done");
		return true;
	}
	
}
