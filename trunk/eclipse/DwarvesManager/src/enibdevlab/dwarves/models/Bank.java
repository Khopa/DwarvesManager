package enibdevlab.dwarves.models;

import java.util.HashMap;

import com.badlogic.gdx.utils.XmlReader.Element;

import enibdevlab.dwarves.models.misc.IPersistent;

/**
 * 
 * Classe qui g�re l'argent du joueur, ses d�penses et revenus mensuels
 * 
 * @author Cl�ment Perreau
 *
 */
public class Bank implements IPersistent{
	
	/**
	 * Revenus
	 */
	private HashMap<String, Integer> income;
	
	/**
	 * D�penses
	 */
	private HashMap<String, Integer> expense;
	
	// NB : Revenus et d�penses sont pas utilis�s dans la version actuelle
	
	/**
	 * Solde courant
	 */
	private int money;
	
	/**
	 * Diamants
	 */
	private int diamonds;
	
	/**
	 * Num�ro de compte
	 */
	// non je rigole :D
	
	/**
	 * Nouvelle banque
	 */
	public Bank(){
		income = new HashMap<String, Integer>();
		expense = new HashMap<String, Integer>();
		money = 0;
		diamonds = 0;
	}
	
	public Bank(Element xmlElement) {
		income = new HashMap<String, Integer>();
		expense = new HashMap<String, Integer>();
		loadFromXmlElement(xmlElement);
	}

	public void addIncome(String name, int value){
		income.put(name, value);
	}
	
	public void addExpense(String name, int value){
		expense.put(name, value);
	}
	
	/**
	 * Modifie une valeur de revenu
	 */
	public void updateIncome(String name, int value){
		if(income.containsKey(name)){
			income.remove(name);
			income.put(name,  value);
		}
		else addIncome(name, value);
	}
	
	/**
	 * Modifie une valeur de d�pense
	 */
	public void updateExpense(String name, int value){
		if(expense.containsKey(name)){
			expense.remove(name);
			expense.put(name,  value);
		}
		else addExpense(name, value);
	}
	
	
	/**
	 * Solde
	 */
	public int getMoney(){
		return money;
	}
	
	
	public void addDiamond(){
		this.diamonds++;
	}
	
	
	/**
	 * Ajout d'argent
	 */
	public void addMoney(int value){
		this.money+=value;
	}
	
	public HashMap<String, Integer> getIncome() {
		return income;
	}

	public HashMap<String, Integer> getExpense() {
		return expense;
	}

	/**
	 * Retrait d'argent
	 * @return false si �a provoque un d�couvert (Dans ce cas l'argent n'est pas retir� et la transaction �choue)
	 */
	public boolean removeMoney(int value){
		if(this.money - value < 0){
			return false;
		}
		else{
			this.money-=value;
			return true;
		}
		
	}
	
	/**
	 * Retrait d'argent
	 * @return false si �a provoque un d�couvert (Dans ce cas l'argent n'est pas retir� et la transaction �choue)
	 */
	public boolean forceRemoveMoney(int value){
		this.money-=value;
		if(this.money < 0) return false;
		else return true;

		
	}

	public int getDiamonds() {
		return diamonds;
	}

	@Override
	public Element saveAsXmlElement() {
		Element output = new Element("Bank", null);
		output.setAttribute("money", Integer.toString(getMoney()));
		output.setAttribute("diamonds", Integer.toString(getDiamonds()));
		return output;
	}

	@Override
	public void loadFromXmlElement(Element xmlElement) {
		this.money = Integer.parseInt(xmlElement.getAttribute("money"));
		this.diamonds = Integer.parseInt(xmlElement.getAttribute("diamonds"));
	}
	
	
}
