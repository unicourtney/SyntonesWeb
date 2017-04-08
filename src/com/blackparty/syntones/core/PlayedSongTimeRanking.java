package com.blackparty.syntones.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.PlayedSongsByTime;

public class PlayedSongTimeRanking {

	public static ArrayList<PlayedSongsByTime> getSongTimeRanking(List<PlayedSongs> played_songs_list) {

		String temp = null;
		ArrayList<PlayedSongsByTime> played_songs_by_time_list = new ArrayList<>();
		;
		DateFormat timeFormat = new SimpleDateFormat("HH");
		int midnightCount = 0, morningCount = 0, noonCount = 0, afternoonCount = 0, nightCount = 0;

		for (int a = 0; a < played_songs_list.size(); a++) {

			String track_id = played_songs_list.get(a).getTrack_id();

			if (temp == null) {

				int playedTime = Integer.parseInt(timeFormat.format(played_songs_list.get(a).getDate()));

				if (playedTime >= 00 && playedTime <= 04) {

					midnightCount++;

				} else if (playedTime >= 05 && playedTime <= 11) {

					morningCount++;

				} else if (playedTime == 12) {

					noonCount++;

				} else if (playedTime >= 13 && playedTime <= 17) {

				
					afternoonCount++;

				} else if (playedTime >= 18 && playedTime <= 23) {

				
					nightCount++;
				}

			} else if (temp.equals(track_id)) {

				int playedTime = Integer.parseInt(timeFormat.format(played_songs_list.get(a).getDate()));

				if (playedTime >= 00 && playedTime <= 04) {

					
					midnightCount++;

				} else if (playedTime >= 05 && playedTime <= 11) {

					
					morningCount++;

				} else if (playedTime == 12) {

					
					noonCount++;

				} else if (playedTime >= 13 && playedTime <= 17) {

			
					afternoonCount++;

				} else if (playedTime >= 18 && playedTime <= 23) {

					nightCount++;
				}

			} else if (!temp.equals(track_id) || a == played_songs_list.size() - 1) {

				PlayedSongsByTime playedSongsByTime = new PlayedSongsByTime();

				playedSongsByTime.setTrack_id(temp);
				playedSongsByTime.setMidnight(midnightCount);
				playedSongsByTime.setMorning(morningCount);
				playedSongsByTime.setNoon(noonCount);
				playedSongsByTime.setAfternoon(afternoonCount);
				playedSongsByTime.setEvening(nightCount);

				// played_songs_by_time_list = new ArrayList<>();

				played_songs_by_time_list.add(playedSongsByTime);

				/*
				 * for (PlayedSongsByTime b : played_songs_by_time_list) {
				 * System.out.println(b.getTrack_id() + " ------ " +
				 * b.getMorning() + " - " + b.getNoon() + " - " +
				 * b.getAfternoon() + " - " + b.getEvening() + " - " +
				 * b.getMidnight()); }
				 */

				midnightCount = 0;
				morningCount = 0;
				noonCount = 0;
				nightCount = 0;
				afternoonCount = 0;

				a--;
			}
			if (a == played_songs_list.size() - 1) {
				PlayedSongsByTime playedSongsByTime = new PlayedSongsByTime();

				playedSongsByTime.setTrack_id(temp);
				playedSongsByTime.setMidnight(midnightCount);
				playedSongsByTime.setMorning(morningCount);
				playedSongsByTime.setNoon(noonCount);
				playedSongsByTime.setAfternoon(afternoonCount);
				playedSongsByTime.setEvening(nightCount);

				// played_songs_by_time_list = new ArrayList<>();

				played_songs_by_time_list.add(playedSongsByTime);

				/*
				 * for (PlayedSongsByTime b : played_songs_by_time_list) {
				 * System.out.println(b.getTrack_id() + " ------ " +
				 * b.getMorning() + " - " + b.getNoon() + " - " +
				 * b.getAfternoon() + " - " + b.getEvening() + " - " +
				 * b.getMidnight()); }
				 */

				midnightCount = 0;
				morningCount = 0;
				noonCount = 0;
				nightCount = 0;
				afternoonCount = 0;
			}

			temp = track_id;
		}

		for (PlayedSongsByTime b : played_songs_by_time_list) {
			System.out.println(b.getTrack_id() + " ------ " + b.getMorning() + " - " + b.getNoon() + " - "
					+ b.getAfternoon() + " - " + b.getEvening() + " - " + b.getMidnight());
		}

		return played_songs_by_time_list;

	}
}
