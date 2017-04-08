package com.blackparty.syntones.core;

import java.util.List;
import java.util.StringTokenizer;

import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.WordBank;

public class SongIDFProcess {

	public static String delimeter = "  \n`~!@#$ %^&*()-_=+[]\\{}|;:\",./<>?";

	public boolean isStopWord(String word, List<String> stpWords) {
		for (String token : stpWords) {
			if (word.equalsIgnoreCase(token)) {
				return true;
			}
		}

		return false;
	}

	public List<WordBank> getIDF(List<WordBank> swbList, int docuCount) {
		for (WordBank word : swbList) {
			float temp = (float) docuCount / (float) word.getMaxCount();
			word.setIdf((float) log2(temp));
			System.out.println("tfIdf >> "+word.getIdf());
		}
		return swbList;
	}

	private double log2(double x) {
		return Math.log(x) / Math.log(2.0d);
	}
	public int titleWeight(String title, String word) {
		StringTokenizer str = new StringTokenizer(title, " ,()&");
		int weight = 0, count = 0;
		while (str.hasMoreTokens()) {
			String token = str.nextToken().trim();
			if (token.equalsIgnoreCase(word)) {
				count += 1;
			}
		}
		weight = count * 20;
		return weight;
	}
	public int artistWeight(String artistName, String word) {
		StringTokenizer str = new StringTokenizer(artistName, " ,()&");
		int weight = 0, count = 0;
		while (str.hasMoreTokens()) {
			String token = str.nextToken().trim();
			if (token.equalsIgnoreCase(word)) {
				count += 1;
			}
		}
		weight = count * 10;
		return weight;
	}
	
	public String delimeter(){
		return delimeter;
	}
}
