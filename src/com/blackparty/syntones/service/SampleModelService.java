package com.blackparty.syntones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SampleModelDAO;
import com.blackparty.syntones.model.SampleModel;



@Service
public class SampleModelService {

	@Autowired
	private SampleModelDAO sampleModelDAO;
	
	public void addSampleModel(SampleModel sm){
		sampleModelDAO.addSampleModel(sm);
	}
}
