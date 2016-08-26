package com.blackparty.syntones.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.SampleModel;

@Repository
@Transactional
public class SampleModelDAO {
	
	@Autowired
	private SessionFactory sf;
	
	public void addSampleModel(SampleModel sm){
		
		Session session = sf.openSession();
		session.save(sm);
		session.flush();
		session.close();
	}
	
	//@SuppressWarnings("unchecked")
//	public SampleModel getSampleModel(){
//		SampleModel sm = null;
//		Session session = sf.openSession();
//		Query query = session.createQuery("from SAMPLE_MODEL_TBL where");
//		
//	}
	
}
