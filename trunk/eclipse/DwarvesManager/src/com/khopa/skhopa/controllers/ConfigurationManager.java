package com.khopa.skhopa.controllers;

import java.util.HashMap;

/**
 * 
 * Configuration manager
 * 
 * @author Clément Perreau
 *
 */
public abstract class ConfigurationManager {

	/**
	 * Float values
	 */
	protected HashMap<String, Float> floatValues;
	
	/**
	 * Integer values
	 */
	protected HashMap<String, Integer> integerValues;
	
	/**
	 * String values
	 */
	protected HashMap<String, String> stringValues;
	
	/**
	 * Boolean values
	 */
	protected HashMap<String, Boolean> booleanValues;
	
	/**
	 * Singleton
	 */
	private static ConfigurationManager instance;
	
	
	public ConfigurationManager(){
		instance = this;
		floatValues = new HashMap<String, Float>();
		integerValues = new HashMap<String, Integer>();
		stringValues = new HashMap<String, String>();
		booleanValues = new HashMap<String, Boolean>();
		load();
	}
	
	
	public void setValue(String key, float value){
		floatValues.put(key, value);
	}
	
	public void setValue(String key, int value){
		integerValues.put(key, value);
	}
	
	public void setValue(String key, String value){
		stringValues.put(key, value);
	}
	
	public void setValue(String key, Boolean value){
		booleanValues.put(key, value);
	}
	
	public abstract void save();
	
	public abstract void load();
	
	/**
	 * Return float value stored in configuration
	 * @param key
	 * @return
	 */
	public float getFloatValue(String key){
		if(!floatValues.containsKey(key)){
			return 0.5f;
		}
		else{
			return floatValues.get(key);
		}
	}
	
	/**
	 * Return integer value stored in configuration
	 * @param key
	 * @return
	 */
	public int getIntegerValue(String key){
		return integerValues.get(key);
	}
	
	/**
	 * Return string value stored in configuration
	 * @param key
	 * @return
	 */
	public String getStringValue(String key){
		return stringValues.get(key);
	}
	
	/**
	 * Return boolean value stored in configuration
	 * @param key
	 * @return
	 */
	public Boolean getBooleanValue(String key){
		if(!booleanValues.containsKey(key)){
			return false;
		}
		else{
			return booleanValues.get(key);
		}
	}
	
	/**
	 * Get configuration singleton instance
	 * @return
	 */
	public static ConfigurationManager getInstance(){
		return instance;
	}

	
	
	
	
}
