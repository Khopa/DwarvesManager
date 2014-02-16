package enibdevlab.dwarves.controllers.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.khopa.skhopa.controllers.ScoreComparator;
import com.khopa.skhopa.controllers.ScoreManager;
import com.khopa.skhopa.models.Score;


/**
 * 
 * Gestion des scores pour le jeu Dwarves Manager
 * 
 * @author Clément Perreau
 *
 */
public class DwarvesScoreManager extends ScoreManager {

	/**
	 * Emplacement du fichier de sauvegarde
	 */
	private final static String savePath = "DwarvesManager/Scores/score.xml";
	
	/**
	 * Exemple file :
	 * <score>
	 * 	<level name = "tuto1">
	 * 		<score author = "Khopa">3601</score>
	 *  </level>
	 * 	... etc
	 * </score>
	 */
	
	/**
	 * Fichier des scores xml
	 */
	private Element scores;
	
	/**
	 * Offline or Online
	 */
	@SuppressWarnings("unused")
	private boolean online = false;
	
	/**
	 * Cree un gestionnaire de score
	 * @param online Stockage online ou pas des scores
	 */
	public DwarvesScoreManager(boolean online){
		super();
		this.online = online;
		this.preLoad();
	}
	
	/**
	 * Cree un gestionnaire offline de scores
	 */
	public DwarvesScoreManager(){
		this(false);
	}
	
	/**
	 * Chargement des scores locaux
	 */
	public void preLoad(){
		
		// Recherche du fichier xml des scores
		FileHandle file = Gdx.files.external(DwarvesScoreManager.savePath);
		
		if(file.exists()){
			XmlReader reader = new XmlReader();
			try {
				this.scores = reader.parse(file);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Si le fichier des scores n'existe pas, on le cree
		this.scores = new Element("score",null);
		
	}
	
	@Override
	public boolean submit(Score score) {
		
		// On cree l'element score
		Element scoreElement = new Element("score", null);
		scoreElement.setText(Integer.toString(score.getScore()));
		scoreElement.setAttribute("author", score.getAuthor());
		
		Array<Element> levels = this.scores.getChildrenByName("level");
		
		/**
		 * On recherche parmi tous les elements level celui qui correspond
		 */
		boolean found = false;
		for(Element level:levels){
			System.out.println(level.getAttribute("name"));
			System.out.println(score.getLevel());
			if(level.getAttribute("name").equals(score.getLevel())){
				level.addChild(scoreElement);
				found = true;
				Array<Element> scoreElements = level.getChildrenByName("score");
				
				// limiter a 10 le nombre de score enregistrés
				if(scoreElements.size>=10){
					int maximum = Integer.MIN_VALUE;
					Element maxScoreElement = null;
					for(Element element:scoreElements){
						int value = Integer.parseInt(element.getText());
						if(value > maximum){
							maximum = value;
							maxScoreElement = element;
						}
					}
					level.removeChild(maxScoreElement);
				}
				break;
			}
		}
		
		// Si pas d'element trouvé, on le crée
		if(!found){
			Element level = new Element("level", null);
			level.setAttribute("name", score.getLevel());
			level.addChild(scoreElement);
			scores.addChild(level);
		}
		
		// Sauvegarde du score
		FileHandle file = Gdx.files.external(DwarvesScoreManager.savePath);
		file.writeString(scores.toString(), false);
		
		return false;
	}

	@Override
	public List<Score> getLevelScore(String level, int limit) {
		
		ArrayList<Score> result = new ArrayList<Score>();
		Array<Element> levels = this.scores.getChildrenByName("level");
		
		// NB : dans notre cas on cherche les scores les plus petits (car c'est un temps)
		int maximum = 0;
		Score maxScore = null;
		for(Element lvl:levels){
			if(lvl.getAttribute("name").equals(level)){
				Array<Element> scoresElement = lvl.getChildrenByName("score");
				
				for(Element scoreElement:scoresElement){
					int currentScore = Integer.parseInt(scoreElement.getText());
					if(result.size()<limit){
						result.add(new Score("local", level, currentScore));
					}
					else{
						maximum = Integer.MIN_VALUE;
						for(Score sco:result){
							if(sco.getScore()>maximum){
								maximum = sco.getScore();
								maxScore = sco;
							}
						}
						if(currentScore > maximum) continue;
						result.remove(maxScore);
						result.add(new Score("local", level, currentScore));
					}
				}
			}
		}
		
		Collections.sort(result, new ScoreComparator());
		
		return result;
	}

	@Override
	public Score getAuthorBestScore(String author, String level) {
		List<Score> scores = getLevelScore(level, 1);
		if(scores.size() > 0) return scores.get(0);
		else return null;
	}

}
