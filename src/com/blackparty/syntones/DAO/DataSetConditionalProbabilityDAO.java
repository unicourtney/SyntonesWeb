package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.DataSetConditionalProbability;
import com.blackparty.syntones.model.DataSetMood;

@Repository
@Transactional
public class DataSetConditionalProbabilityDAO {
	@Autowired
	private SessionFactory sf;
	
	public boolean save(List<DataSetMood> moodList)throws Exception{
		for(DataSetMood m: moodList){
			DataSetConditionalProbability p = new DataSetConditionalProbability();
			p.setMoodId(m.getId());
			for(DataSetConditionalProbability d:m.getConditionalProbabilities()){
				Session session = sf.openSession();
				p.setConditionalProbability(d.getConditionalProbability());
				p.setWord(d.getWord());
				session.save(p);
				session.flush();
				session.close();
			}
		}
		return true;
	}
	
	public List<DataSetMood> setMood(List<DataSetMood> moodList) throws Exception{
		for(DataSetMood s:moodList){
			//System.out.println("Getting values for mood: "+s.getMoodName() +"  ||  "+s.getId());
			List<DataSetConditionalProbability> cpList = getList(s.getId());
			s.setConditionalProbabilities(cpList);
		}
		return moodList;
	}
	
	public List<DataSetConditionalProbability> getList(int id)throws Exception{
		String sql = "from DataSetConditionalProbability where mood_id =:id";
		Session session = sf.openSession();
		Query query = session.createQuery(sql);
		query.setInteger("id", id);
		List<DataSetConditionalProbability> list = query.list();
		session.clear();
		session.flush();
		session.close();
		
		return list;
	}
}
