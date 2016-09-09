package com.blackparty.syntones.endpoint;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.response.SearchResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import org.springframework.http.MediaType;

@RestController
@Component
public class NavigationEndpoint {

	@RequestMapping(value = "search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse search(@RequestBody String searchString) {
		// wala pa ni siya gamit
		System.out.println("recived search request");
		SearchResponse sr = new SearchResponse();
		Message message = new Message();
		message.setMessage("search request \"" + searchString + "\"has been received.");
		sr.setMessage(message);
		return sr;
	}

	@RequestMapping(value = "/test")
	public String toIndex() {
		System.out.println("user dir:" + System.getProperty("user.dir"));
		return "index";
	}

	@RequestMapping(value = "/listen", produces = "audio/mp3", method = RequestMethod.GET)
	public Response listen(@HeaderParam("Range") String range) {
		System.out.println("Received request to listen.");
		String audio = "D:/Our_Files1/Eric/School/Thesis/Syntones/Songs/Uploaded/50450/500700.mp3";
		File file = new File(audio);
		MediaResource mediaResource = new MediaResource();
		Response Rresponse = null;
		try{
			Rresponse = mediaResource.buildStream(file, range);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return Rresponse;
	}

}
