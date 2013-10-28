package enibdevlab.dwarves.models.characters;

import java.util.Random;

import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.Difficulty;
import enibdevlab.dwarves.models.misc.IPersistent;

/**
 * 
 * Besoin du nain
 * 
 * @author Clément Perreau
 *
 */
public class Needs implements IPersistent {
	
	/**
	 * Random
	 */
	private static Random random = new Random();
	
	/**
	 * Besoin Maximum
	 */
	private static int MAX = 40000;
	
	/**
	 * Besoin Minimum
	 */
	private static int MIN = 0;
	
	/**
	 * Pas d'incrémentation nominal des besoins
	 */
	private static int STEP = 1;
	
	/**
	 * Moyenne des niveau de besoin aléatoire
	 */
	private static float MEAN = 2.5f;

	/**
	 * Variance des niveau de besoin aléatoire
	 */
	private static float DEVIATION = 1.0f;
	
	/**
	 * Faim (MIN<=hunger<=MAX)
	 */
	protected int beer;
	
	/**
	 * Besoin de sommeil (MIN<=sleep<=MAX)
	 */
	protected int sleep;
	
	/**
	 * Prédisposition au besoin de sommeil
	 */
	protected double sleepNeed;
	
	/**
	 * Prédisposition au besoin de bière
	 */
	protected double beerNeed;
	
	/**
	 * Nain concerné
	 */
	protected Dwarf dwarf;


	/**
	 * Constructeur
	 * @param hunger Niveau de faim initial
	 * @param sleep Niveau de besoin sommeil initial
	 */
	public Needs(Dwarf dwarf){
		this.dwarf = dwarf;
		this.setThirst(0);
		this.setSleep(0);
		randomGeneration();
	}
	
	/**
	 * Initialise les paramètres de comportement aléatoire
	 */
	private void randomGeneration(){
		sleepNeed =  randomGaussianNeedFactor();
		beerNeed  =  randomGaussianNeedFactor();
	}
	
	private static double randomGaussianNeedFactor(){
		double result = random.nextGaussian()*DEVIATION + MEAN;
		if(result<=0.5) result=0.5;
		else if (result >= MEAN+2*DEVIATION)result = MEAN+2*DEVIATION;
		return result;
	}
	
	public int getThirst() {
		return beer;
	}

	public void setThirst(int beer) {
		if (beer>MAX) beer = MAX;
		else if (beer < MIN) beer = MIN;
		this.beer = beer;
	}


	public int getSleep() {
		return sleep;
	}

	public void setSleep(int sleep) {
		if (sleep>MAX) sleep = MAX;
		else if (sleep < MIN) sleep = MIN;
		this.sleep = sleep;
	}
	
	public void update(){
		
		setSleep((int) (this.sleep + STEP*this.sleepNeed));
		setThirst((int) (this.beer + STEP*this.beerNeed));
		
		if(!Difficulty.areNeedsToManage()){
			setThirst(0);
			setSleep(0);
		}
		
		if(this.dwarf.getJobName().equals("Tavernier")) setThirst(0);
	}

	/*
	 * Dit si l'energie du nain est au max, utile pour savoir quand sortir du sommeil
	 */
	public boolean hasMaxEnergy(){
		if(this.sleep <= 0) return true;
		return false;
	}
	
	/**
	 * Dit si le nain est actuellement prêt à reprendre le travail
	 * 
	 * Il faut que les valeurs de sommeil et de soif soit quand même un peu au dessus du seuil, histoire
	 * que les nains ne s'arrêtent pas de suite.
	 * 
	 */
	public boolean isSatisfied(){
		return (this.sleep<2.5f/5f*MAX && this.beer<2.5f/5f*MAX);
	}
	
	/**
	 * Dit si le nain peut travailler dans son état actuel
	 */
	public boolean isAbleToWork(){
		return (this.sleep<4f/5f*MAX && this.beer<4f/5f*MAX);
	}

	/**
	 * Retourne la valeur du seuil à partir duquel il devient critique d'aller dormir
	 */
	public int getSleepThreshold() {
		return (int) (3f/4f*MAX);
	}
	
	/**
	 * Retourne la valeur du seuil à partir duquel il devient critique d'aller boire
	 */
	public int getThirstThreshold() {
		return (int) (3f/4f*MAX);
	}
	
	public void setSleepNeed(double sleepNeed) {
		this.sleepNeed = sleepNeed;
	}

	public void setBeerNeed(double beerNeed) {
		this.beerNeed = beerNeed;
	}

	public int getMaxValue(){
		return MAX;
	}

	public void addSleep(int sleep) {
		this.setSleep(this.getSleep()+sleep);
	}
	
	public void addThirst(int thirst) {
		this.setThirst(this.getThirst()+thirst);
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("needs", null);
		output.setAttribute("sleep", Integer.toString(sleep));
		output.setAttribute("beer", Integer.toString(beer));
		output.setAttribute("sleepNeed", Double.toString(sleepNeed));
		output.setAttribute("beerNeed", Double.toString(beerNeed));
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		// TODO Auto-generated method stub
		
	}
	

}
