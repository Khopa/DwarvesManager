package com.khopa.skhopa.controllers;

import com.khopa.skhopa.models.Score;


/**
 * 
 * Score manager as a Singleton
 * 
 * @author Clément Perreau
 *
 */
public abstract class ScoreManager {

	/**
	 * Single instance
	 */
	private static ScoreManager instance;
	
	/**
	 * Create a ScoreManager
	 */
	public ScoreManager(){
		ScoreManager.instance = this;
	}
	
	/**
	 * Submit score online
	 * @param score Score object to submit
	 * @return Whether the operation worked or not
	 */
	public abstract boolean submit(Score score);
	
	/**
	 * Return a list of score for the current level
	 * @param level Level name or String ID
	 * @param limit Limit of score (E.g : only the 10 best score, if < 0, all the scores)
	 */
	public abstract Score getLevelScore(String level, int limit);

	/**
	 * Return the best score from the given author on the given level
	 * @param author Author name
	 * @param level Level name
	 * @return Author best score instance
	 */
	public abstract Score getAuthorBestScore(String author, String level);
	
	/**
	 * Get ScoreManager instance
	 */
	public ScoreManager getInstance(){
		return instance;
	}
	
	
}
