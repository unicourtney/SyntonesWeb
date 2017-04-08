package com.blackparty.syntones.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.Song;

public class PlaylistGenerator {

	public String part_of_day(int current_time) {
		String part_of_day = "";

		if (current_time >= 00 && current_time <= 04) {

			part_of_day = "midnight";

		} else if (current_time >= 05 && current_time <= 11) {

			part_of_day = "morning";

		} else if (current_time == 12) {

			part_of_day = "noon";

		} else if (current_time >= 13 && current_time <= 17) {

			part_of_day = "afternoon";

		} else if (current_time >= 18 && current_time <= 23) {

			part_of_day = "night";

		}

		return part_of_day;
	}

	public List<Long> generate(int numberOfSession, Map<Long, Song> songMap, List<PlayedSongs> playedSongList) {
		// generate playlist for morning

		System.out.println("number of sessions: " + numberOfSession);
		// seeting map for songCount
		Map<Long, Integer> songMapCount = new HashMap<>();
		// setting map for mood
		Map<String, Integer> moodMap = new HashMap<>();
		// setting map for genre
		Map<Long, Integer> genreMap = new HashMap<>();

		for (PlayedSongs s : playedSongList) {
			Song song = songMap.get(Long.parseLong(s.getTrack_id()));
			if (!songMapCount.containsKey(song.getSongId())) {
				songMapCount.put(song.getSongId(), 1);
			} else {
				songMapCount.put(song.getSongId(), songMapCount.get(song.getSongId()) + 1);
			}
			if (!moodMap.containsKey(song.getMood())) {
				moodMap.put(song.getMood(), 1);
			} else {
				moodMap.put(song.getMood(), moodMap.get(song.getMood()) + 1);
			}
			if (!genreMap.containsKey(song.getGenre())) {
				genreMap.put(song.getGenreId(), 1);
			} else {
				int lol = genreMap.get(song.getGenre());
				genreMap.put(song.getGenreId(), lol + 1);
			}
		}

		Map<Long, Double> songWeightMap = new HashMap<Long, Double>();
		for (Long l : songMap.keySet()) {
			Song song = songMap.get(l);
			int moodCount = 0;
			int genreCount = 0;
			int songCount = 0;
			if (songMapCount.containsKey(song.getSongId())) {
				songCount = songMapCount.get(song.getSongId());
			}
			if (moodMap.containsKey(song.getMood())) {
				moodCount = moodMap.get(song.getMood());
			}
			if (genreMap.containsKey(song.getGenre())) {
				genreCount = genreMap.get(song.getGenre());
			}
			System.out.println(song.getSongId() + " || " + moodCount + "/" + numberOfSession + "+.01 " + genreCount
					+ " / " + numberOfSession + "+.01 ");
			double a = (double) moodCount / numberOfSession + .01;
			double b = (double) genreCount / numberOfSession + .01;
			double c = (double) songCount / playedSongList.size() + .01;
			double wieght = a + b + c;
			songWeightMap.put(song.getSongId(), wieght);
		}

		Map<Long, Double> sortedMap = sortByValue(songWeightMap);
		List<Long> songList = new ArrayList<>();
		int counter = 0;
		for (Long l : sortedMap.keySet()) {
			System.out.println(l + " || " + songWeightMap.get(l));
			songList.add(l);
			counter++;
			if (counter > 9) {
				break;
			}
		}

		return songList;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> unsortMap) {

		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				// return (o1.getValue()).compareTo(o2.getValue()); //ascending
				return (o2.getValue().compareTo(o1.getValue())); // descending
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;

	}

}
