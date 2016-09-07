package com.blackparty.syntones.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.service.ArService;



public class ArTestController {
	@Autowired
	ArService arService;
	
	@RequestMapping(value = "/playSong")
	public ModelAndView playSongPage(){
		
		ModelAndView mav = new ModelAndView("playSong", "message", "-- I'M IN PLAYSONG JSP --");
		return mav;
		
	}
}
