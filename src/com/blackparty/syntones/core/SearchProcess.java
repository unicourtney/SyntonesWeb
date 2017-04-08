package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.blackparty.syntones.model.SongWordBank;

public class SearchProcess {

	public static String delimeter = "  ' \n`~!@#$ %^&*()-_=+[]\\{}|;:\",./<>?";

	public int parseWord(List<SongWordBank> songWords) {
		long tempId = 0;
		int flag = 0, c = 0;
		for (SongWordBank sw : songWords) {
			System.out.println("dzuuud");
			if (c == 0) {
				tempId = sw.getWordId();
				flag += 2;
				c++;
			} else {
				tempId = tempId + 1;
				if (sw.getWordId() == tempId) {
					flag += 2;
					System.out.println("added");
					c++;
				} else {
					if (flag > 2) {
						flag -= 1;
					}
					c++;
				}
			}
		}
		return flag;
	}

	public ArrayList<String> processAS(String searchWord) {
		searchWord = searchWord.replace("[", "").replace("]", "");
		Boolean qflag = false;
		String temp = "";
		ArrayList<String> swList = new ArrayList<String>();
		StringTokenizer str = new StringTokenizer(searchWord, " ");
		while (str.hasMoreTokens()) {
			String token = str.nextToken();
			System.out.println("token >> " + token);
			if (!qflag && token.contains("\"")) {
				System.out.println("started with \"");
				qflag = true;
				temp = token + " ";
				int flag = 0;
				char[] checker = temp.toCharArray();
				for (int i = 0; i < checker.length; i++) {
					if (checker[i] == '"') {
						flag += 1;
					}
				}
				if (flag >= 2) {
					qflag = false;
					swList.add(temp);
					temp = "";
				}
			} else if (qflag && !token.contains("\"")) {
				temp += token + " ";
			} else if (qflag && token.contains("\"")) {
				System.out.println("ended with \"");
				temp += token;
				qflag = false;
				swList.add(temp);
				temp = "";
			} else if (!qflag && !token.contains("\"")) {
				System.out.println("maybe AND || OR");
				temp += token;
				swList.add(temp);
				temp = "";
			} else {
				temp += token + " ";
			}
		}

		System.out.println("start");
		for (String string : swList) {
			System.out.println(string);
		}
		return swList;
	}

	public boolean isStopWord(String word, List<String> stpWords) {
		for (String token : stpWords) {
			if (word.equalsIgnoreCase(token)) {
				return true;
			}
		}

		return false;
	}

}
