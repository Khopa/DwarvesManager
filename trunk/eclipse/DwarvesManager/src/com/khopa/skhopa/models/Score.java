package com.khopa.skhopa.models;


/**
 * 
 * Score model
 * 
 * @author Clément Perreau
 *
 */
public class Score {
	
	/**
	 * Score Author name
	 */
	protected String author;
	
	/**
	 * Level
	 */
	protected String level;
	
	/**
	 * Key
	 */
	protected int key;
	
	/**
	 * Score value
	 */
	protected int score;
	
	/**
	 * Create a score
	 * @param author Author of the score 
	 * @param key Key generated to identify the author
	 * @param score Score value
	 */
	public Score(String author, String level, int key, int score){
		this.author = author;
		this.level = level;
		this.key = key;
		this.score = score;
	}
	
	/**
	 * Create a score
	 */
	public Score(String author, String level, int score){
		this(author, level, -1, score);
	}

	public String getAuthor() {
		return author;
	}

	public String getLevel() {
		return level;
	}

	public int getKey() {
		return key;
	}

	public int getScore() {
		return score;
	}
	
}
