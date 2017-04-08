package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;

import com.blackparty.syntones.model.DataSetConditionalProbability;
import com.blackparty.syntones.model.DataSetMood;
import com.blackparty.syntones.model.DataSetSong;
import com.blackparty.syntones.model.DataSetWord;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.TracksMetadata;
import com.blackparty.syntones.service.DataSetMoodService;
import com.blackparty.syntones.service.DataSetSongService;

public class NaiveBayes {

	@Autowired
	private DataSetSongService dss;
	@Autowired
	private DataSetMoodService dsm;

	public NaiveBayes() {
	}

	public boolean train() throws Exception {
		boolean flag = true;
		// get all mood from the db
		List<DataSetMood> moodList = dsm.getAllMood();

		// get all songs on the dataset
		List<DataSetSong> songList = dss.getAllSongs();

		// creating a list of words coming from the lyrics of all songs
		List<String> wordList = listAllWords(songList);

		// get priors and place them on the respective mood
		List<DataSetMood> moodListUpdated = getPriors(songList, moodList);

		// get conditional probability
		getConditionalProbability(moodListUpdated, songList);
		dsm.updateBatchAllMood(moodListUpdated);

		return flag;
	}

	public List<DataSetMood> getConditionalProbability(List<DataSetMood> moodList, List<DataSetSong> songList) {
		System.out.println("Getting all conditional probability..");
		// list all words and place them on a map
		List<String> wordList = listAllWords(songList);
		Map<String, Integer> countWC = new HashMap<>();

		// display all words on the list.
		/*
		 * for (String s : wordList) { System.out.println(">> " + s); }
		 */

		int vocabularyNumber = wordList.size();
		for (DataSetMood e : moodList) {
			for (String s : wordList) {
				countWC.put(s, 0);
			}
			int countW = 0;
			String mood = e.getMoodName();
			for (DataSetSong s : songList) {
				System.out.println("song id: " + s.getNumber());
				String temp = s.getMood();
				if (mood.equalsIgnoreCase(temp)) {
					String lyrics = s.getLyricsCleaned();
					StringTokenizer token = new StringTokenizer(lyrics);
					while (token.hasMoreTokens()) {
						String word = token.nextToken().trim().toLowerCase();
						countWC.put(word, countWC.get(word) + 1);
						countW++;
					}
				}
			}
			// gamita lang ni para mo output. gi comment lang nako kay samok sa
			// console
			Set set = countWC.entrySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				System.out.println(entry.getKey() + " || " + entry.getValue());
			}
			System.out.println("count(" + e.getMoodName() + ")" + countW);

			List<DataSetConditionalProbability> conditionalProbabilityResult = new ArrayList<>();
			for (String s : wordList) {
				DataSetConditionalProbability dcp = new DataSetConditionalProbability();
				dcp.setWord(s);
				dcp.setMoodId(e.getId());
				System.out.println("Conditional probabilities for mood: " + s);
				System.out.print("P(" + s + "|" + e.getMoodName() + ") = ");
				double cpr = (double) (countWC.get(s) + 1) / (countW + vocabularyNumber);
				dcp.setConditionalProbability(cpr);
				System.out.println(cpr + " = " + countWC.get(s) + " + 1 / " + countW + " + " + vocabularyNumber);
				conditionalProbabilityResult.add(dcp);
			}
			e.setConditionalProbabilities(conditionalProbabilityResult);
		}
		System.out.println("done");
		return moodList;

	}

	public List<DataSetMood> getPriors(List<DataSetSong> songList, List<DataSetMood> moodList) {
		System.out.println("Getting priors for each mood: ");

		Map<String, Integer> moodMap = new HashMap<>();
		for (DataSetMood m : moodList) {
			moodMap.put(m.getMoodName(), 0);
		}

		for (DataSetSong s : songList) {
			String mood = s.getMood().trim().toLowerCase();
			// System.out.println("> " + temp);
			if (moodMap.containsKey(mood)) {
				moodMap.put(mood, moodMap.get(mood) + 1);
			}

		}

		// calculate for the priors and place them on objectlist
		for (DataSetMood ml : moodList) {
			double prior = ((double) (int) moodMap.get(ml.getMoodName()) / songList.size());
			ml.setPrior(prior);
			// System.out.print(ml.getMoodName() + " || " +
			// moodMap.get(ml.getMoodName()) + " / " + songList.size());
			// System.out.println(" = " + ml.getPrior());
		}
		System.out.println("DONE");
		// Runtime.getRuntime().exit(1);
		return moodList;
	}

	public List<String> listAllWords(List<DataSetSong> songList) {
		System.out.print("LISTING ALL WORDS.....");
		List<String> wordList = new ArrayList<String>();
		String line;
		try {

			Map<String, String> wordMap = new HashMap<>();
			for (DataSetSong s : songList) {
				StringTokenizer token = new StringTokenizer(s.getLyricsCleaned());
				while (token.hasMoreTokens()) {
					String word = token.nextToken();
					if (!wordMap.containsKey(word)) {
						wordMap.put(word, word);
					}
				}
			}
			Set set = wordMap.entrySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				wordList.add((String) entry.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("DONE");
		return sortInAlphabetical(wordList);
	}

	public List<String> sortInAlphabetical(List<String> wordList) {
		Collections.sort(wordList, ALPHABETICAL_ORDER);
		return wordList;
	}

	private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
		public int compare(String str1, String str2) {
			int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
			if (res == 0) {
				res = str1.compareTo(str2);
			}
			return res;
		}
	};

	public Map.Entry classify(Song song, List<DataSetWord> dataSetWordList, List<DataSetMood> moodList,
			List<String> stopWordList) {
		System.out.println("Attempting to classify: " + song.getSongTitle());
		Map<String, String> stopWordsMap = new HashMap<>();
		for (String s : stopWordList) {
			stopWordsMap.put(s, s);
		}

		// clean the lyrics first
		String lyrics = song.getLyrics();
		lyrics = lyrics.replace("\\n", "");
		StringTokenizer st = new StringTokenizer(lyrics, " ");
		String cleanLyrics = "";
		while (st.hasMoreTokens()) {
			String temp = st.nextToken().replaceAll("(?!\\'|-)[^A-Za-z0-9]", " ").toLowerCase();
			if (stopWordsMap.containsKey(temp)) {
				continue;
			}
			cleanLyrics = cleanLyrics.concat(temp + " ");
		}
		cleanLyrics = cleanLyrics.replaceAll("'s", "");
		System.out.println("CLEAN LYRICS" + cleanLyrics);
		// Runtime.getRuntime().exit(1);

		// get list of words, place them on a map
		List<String> wordList = new ArrayList<>();
		for (DataSetWord d : dataSetWordList) {
			wordList.add(d.getWord());
		}

		Map<String, Double> testResultMap = new HashMap<>();

		for (DataSetMood m : moodList) {
			StringTokenizer tokenizer = new StringTokenizer(cleanLyrics);
			List<DataSetConditionalProbability> conditionalProbability = m.getConditionalProbabilities();
			Map<String, Double> conditionalProbabilityMap = new HashMap<>();
			for (DataSetConditionalProbability s : conditionalProbability) {
				conditionalProbabilityMap.put(s.getWord(), s.getConditionalProbability());
			}
			// getting the result
			double prior = Math.log(m.getPrior());
			double result = 0;
			System.out.println("prior of: " + m.getMoodName() + " = " + m.getPrior());
			while (tokenizer.hasMoreTokens()) {
				String s = tokenizer.nextToken();
				if (conditionalProbabilityMap.containsKey(s)) {
					double a = conditionalProbabilityMap.get(s);
					System.out.println(s + " >> " + a);
					result = result + Math.log(a);
					System.out.println("result = " + result);
				}
			}
			result = result + prior;
			testResultMap.put(m.getMoodName(), result);
		}

		Map.Entry<String, Double> max = null;
		for (Map.Entry<String, Double> entry : testResultMap.entrySet()) {
			System.out.println("mood = " + entry.getKey() + "  ||  value = " + entry.getValue());
			if (max == null || entry.getValue().compareTo(max.getValue()) > 0) {
				max = entry;
			}
		}

		System.out.println(
				"THE MOOD OF " + song.getSongTitle() + " IS " + max.getKey() + " with a value of " + max.getValue());
		return max;

	}

	public Map.Entry<String, Double> classify(TracksMetadata tracksMetadata, List<DataSetWord> dataSetWordList,
			List<DataSetMood> moodList) {

		System.out.println("Attempting to classify the test data: ");
		// get list of words, place them on a map
		List<String> wordList = new ArrayList<>();
		for (DataSetWord d : dataSetWordList) {
			wordList.add(d.getWord());
		}

		Map<String, Double> testResultMap = new HashMap<>();

		for (DataSetMood m : moodList) {
			StringTokenizer tokenizer = new StringTokenizer(tracksMetadata.getLyricsCleaned());
			List<DataSetConditionalProbability> conditionalProbability = m.getConditionalProbabilities();
			Map<String, Double> conditionalProbabilityMap = new HashMap<>();
			for (DataSetConditionalProbability s : conditionalProbability) {
				conditionalProbabilityMap.put(s.getWord(), s.getConditionalProbability());
			}
			// getting the result
			double prior = Math.log(m.getPrior());
			double result = 0;
			System.out.println("prior of: " + m.getMoodName() + " = " + m.getPrior());
			while (tokenizer.hasMoreTokens()) {
				String s = tokenizer.nextToken();
				if (conditionalProbabilityMap.containsKey(s)) {
					double a = conditionalProbabilityMap.get(s);
					System.out.println(s + " >> " + a);
					result = result + Math.log(a);
					System.out.println("result = " + result);
				}
			}
			result = result + prior;
			testResultMap.put(m.getMoodName(), result);
		}

		// Set set = testResultMap.entrySet();
		// Iterator iterator = set.iterator();
		// while (iterator.hasNext()) {
		// Map.Entry entry = (Map.Entry) iterator.next();
		// System.out.print(entry.getKey() + " || " + entry.getValue());
		// System.out.printf(" || %.09f \n",entry.getValue());
		//
		// }

		Map.Entry<String, Double> max = null;
		for (Map.Entry<String, Double> entry : testResultMap.entrySet()) {
			if (max == null || entry.getValue().compareTo(max.getValue()) > 0) {
				max = entry;
			}
		}

		System.out.println("test data is " + max.getKey() + " with a value of " + max.getValue());
		return max;
	}
	
	
}
