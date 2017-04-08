package com.blackparty.syntones.core;

import java.util.List;

import com.blackparty.syntones.model.ArtistWordBank;

public class ArtistIDFProcess {
	public static String delimeter = "  \n`~!@#$ %^&*()-_=+[]\\{}|;:\",./<>?";
	public static Stemmer stem = new Stemmer();

	public List<ArtistWordBank> getIDF(List<ArtistWordBank> swbList, int docuCount) {
		for (ArtistWordBank word : swbList) {
			float temp = (float) docuCount / (float) word.getMaxCount();
			word.setIdf((float) log2(temp));
		}
		return swbList;
	}

	private double log2(double x) {
		return Math.log(x) / Math.log(2.0d);
	}

}
