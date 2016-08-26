package com.blackparty.syntones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.model.SampleModel;
import com.blackparty.syntones.service.SampleModelService;

@Controller
public class MainController {
	@Autowired
	SampleModelService sampleModelService;
	
	
	@RequestMapping(value = "/")
	public String defaultPage(){
		return "index";
		
	}
	@RequestMapping(value = "/index")
	public ModelAndView indexPage() {
		ModelAndView mav = new ModelAndView("index", "message", "Running MainController.index().");

		return mav;
	}
	
		
	
	
	/*@RequestMapping(value = "/upload")
	public ModelAndView save(@RequestParam(value = "input") String input){
		ModelAndView mav = new ModelAndView("index");
		SampleModel sm = new SampleModel(input);
		sampleModelService.addSampleModel(sm);
		return mav;
	}*/
	
}
