package com.khopa.skhopa.controllers;

import java.util.Comparator;

import com.khopa.skhopa.models.Score;

public class ScoreComparator implements Comparator<Score> {

	@Override
	public int compare(Score sc1, Score sc2) {
		if(sc1.getScore() < sc2.getScore()) return -1;
		else if(sc1.getScore() == sc2.getScore()) return 0;
		else return 1;
	}

}
