package com.khopa.skhopa.models;


/**
 * 
 * Score model
 * 
 * @author Clément Perreau
 *
 */
public abstract class Score {
	
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
	public Score(String author, int key, int score){
		this.author = author;
		this.key = key;
		this.score = score;
	}
	
	/**
	 * Create a score
	 */
	public Score(String author, int score){
		this(author, -1, score);
	}
	
}
