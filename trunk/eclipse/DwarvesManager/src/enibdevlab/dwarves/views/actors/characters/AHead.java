package enibdevlab.dwarves.views.actors.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.views.Tileset;
import enibdevlab.dwarves.views.actors.characters.config.BodyConfig;

public class AHead extends ACharacterPart {

	/**
	 * Liste de sprites pour les différentes directions de la tête
	 */
	protected Tileset tileset;
	
	/**
	 * Position de la pupille (par rapport au centre de l'oeil)
	 */
	protected Vector2 pupilPosition = new Vector2();
	
	/**
	 * Valeur booléene qui gère le clignement des yeux
	 */
	protected boolean blink = false;
	
	/**
	 * Alias pour simplifier les expressions (=this.acharacter.bodyConfig)
	 */
	protected BodyConfig bodyConfig;
	
	/**
	 * 
	 * Construit une tête pour un personnage
	 * 
	 * @param acharacter  Référence vers la vue principale
	 * @param tileset Tileset de la tête
	 */
	public AHead(ACharacter acharacter, Tileset tileset) {
		super(acharacter);
		this.tileset = tileset;
		this.bodyConfig = acharacter.bodyConfig;
		this.setPosition(bodyConfig.getHeadConfig().getPosition().x + bodyConfig.getBodyTileset().getTileWidth()/4 + bodyConfig.getHeadConfig().getHeadTileset().getTileWidth()/2,
				         bodyConfig.getHeadConfig().getPosition().y + bodyConfig.getHeadConfig().getHeadTileset().getTileHeight()/2);
		this.setSize(this.tileset.getTileWidth(), this.tileset.getTileHeight());
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha){
		
		super.draw(batch, parentAlpha);
		
		this.setColor(acharacter.getColor());
		Color tmp = batch.getColor();
		batch.setColor(this.getColor());
		
		// Dessin du sprite principal
		batch.draw(this.tileset.getTile(this.acharacter.direction.toInt()),
				this.getX()-bodyConfig.getHeadConfig().getHeadTileset().getTileWidth()/2,
				this.getY()-bodyConfig.getHeadConfig().getHeadTileset().getTileHeight()/2,
				this.getOriginX(),this.getOriginY(),
				this.getWidth(), this.getHeight(), this.getScaleX(),
				this.getScaleY(), this.getRotation());
		
		// Pour les yeux et la bouche, ça change selon les cas
		switch(this.acharacter.direction){
			case BOTTOM:
				this.drawBottom(batch, parentAlpha);
			break;
			case LEFT:
				this.drawLeft(batch, parentAlpha);
			break;
			case TOP:
				this.drawTop(batch, parentAlpha);
			break;
			case RIGHT:
				this.drawRight(batch, parentAlpha);
			break;
			default:
				this.drawBottom(batch, parentAlpha);
		}
		batch.setColor(tmp);
	}
	
	/**
	 * 
	 * Dessine la tête du personnage quand il se dirige vers le bas (face au joueur) (dessin des deux yeux et bouche)
	 * 
	 * @param batch Batch 
	 * @param delta Temps ecoulé depuis le dernier dessin
	 */
	protected void drawBottom(SpriteBatch batch, float parentAlpha){

		float tileW = bodyConfig.getEyesConfig().getEyesTileset().getTileWidth();
		float tileH = bodyConfig.getEyesConfig().getEyesTileset().getTileHeight();
		float posx = bodyConfig.getEyesConfig().getPosition().x;
		float posy = bodyConfig.getEyesConfig().getPosition().y;
		
		Tileset tileset = bodyConfig.getEyesConfig().getEyesTileset();
		TextureRegion eye;
		if (blink) eye=tileset.getTile(1);
		else eye=tileset.getTile(0);
		
		// Oeil droit
		batch.draw(eye,
				   this.getX()+ posx - tileW/2,
				   this.getY()+ posy - tileH/2,
				   this.getOriginX(),this.getOriginY(),
				   tileW,
				   tileH,
				   this.getScaleX(),
				   this.getScaleY(), this.getRotation());
		
		if (!blink) batch.draw(tileset.getTile(2),
				    this.getX()+ posx - tileW/2 + pupilPosition.x,
				    this.getY()+ posy - tileH/2 + pupilPosition.y,
				    this.getOriginX(),this.getOriginY(),
				    tileW,
				    tileH,
				    this.getScaleX(),
				    this.getScaleY(), this.getRotation());
		
		
		// Oeil gauche
		batch.draw(eye,
				   this.getX()- posx - tileW/2,
				   this.getY()+ posy - tileH/2,
				   this.getOriginX(),this.getOriginY(),
				   tileW,
				   tileH,
				   this.getScaleX(),
				   this.getScaleY(), this.getRotation());
		
		if (!blink) batch.draw(tileset.getTile(2),
				    this.getX()- posx - tileW/2 + pupilPosition.x,
				    this.getY()+ posy - tileH/2 + pupilPosition.y,
				    this.getOriginX(),this.getOriginY(),
				    tileW,
				    tileH,
				    this.getScaleX(),
				    this.getScaleY(), this.getRotation());
		
		
		/*
		 * décalage de la bouche selon la direction que prends le personnage
		 */

		// bouche
		posx = this.acharacter.bodyConfig.getMouthConfig().getPosition().x;
		posy = this.acharacter.bodyConfig.getMouthConfig().getPosition().y;
		tileW = this.acharacter.bodyConfig.getMouthConfig().getMouthTileset().getTileWidth();
		tileH = this.acharacter.bodyConfig.getMouthConfig().getMouthTileset().getTileHeight();
		batch.draw(this.acharacter.bodyConfig.getMouthConfig().getMouthTileset().getTile(1),
				   this.getX()+ posx - tileW/2,
				   this.getY()+ posy - tileH/2,
				   this.getOriginX(),this.getOriginY(),
				   tileW,
				   tileH,
				   this.getScaleX(),
				   this.getScaleY(), this.getRotation());

	}
	
	/**
	 * 
	 * Dessine la tête du personnage quand il regarde vers le haut (pas de dessin)
	 * 
	 * @param batch Batch 
	 * @param delta Temps ecoulé depuis le dernier dessin
	 */
	protected void drawTop(SpriteBatch batch, float parentAlpha){
	}
	
	/**
	 * 
	 * Dessine la tête du personnage quand il regarde vers la droite (un oeil, une bouche)
	 * 
	 * @param batch Batch 
	 * @param delta Temps ecoulé depuis le dernier dessin
	 */
	protected void drawRight(SpriteBatch batch, float parentAlpha){
		
		// Oeil droit
		
		float tileW = this.acharacter.bodyConfig.getEyesConfig().getEyesTileset().getTileWidth();
		float tileH = this.acharacter.bodyConfig.getEyesConfig().getEyesTileset().getTileHeight();
		float posx = this.acharacter.bodyConfig.getEyesConfig().getPosition().x;
		float posy = this.acharacter.bodyConfig.getEyesConfig().getPosition().y;
		Tileset tileset = this.acharacter.bodyConfig.getEyesConfig().getEyesTileset();
		TextureRegion eye;
		if (blink) eye=tileset.getTile(1);
		else eye=tileset.getTile(0);
		
		batch.draw(eye,
				   this.getX()+ posx - tileW/2,
				   this.getY()+ posy - tileH/2,
				   this.getOriginX(),this.getOriginY(),
				   tileW,
				   tileH,
				   this.getScaleX(),
				   this.getScaleY(), this.getRotation());
		
		if (!blink)batch.draw(tileset.getTile(2),
				   this.getX()+ posx - tileW/2 + this.acharacter.bodyConfig.getEyesConfig().getxRadius(),
				   this.getY()+ posy - tileH/2,
				   this.getOriginX(),this.getOriginY(),
				   tileW,
				   tileH,
				   this.getScaleX(),
				   this.getScaleY(), this.getRotation());
		
		posx = this.acharacter.bodyConfig.getMouthConfig().getPosition().x-3;
		posy = this.acharacter.bodyConfig.getMouthConfig().getPosition().y;
		tileW = this.acharacter.bodyConfig.getMouthConfig().getMouthTileset().getTileWidth();
		tileH = this.acharacter.bodyConfig.getMouthConfig().getMouthTileset().getTileHeight();
		batch.draw(this.acharacter.bodyConfig.getMouthConfig().getMouthTileset().getTile(1),
				   this.getX()+ posx - tileW/2 + this.acharacter.bodyConfig.getMouthConfig().getSidePosition().x,
				   this.getY()+ posy - tileH/2 + this.acharacter.bodyConfig.getMouthConfig().getSidePosition().y,
				   this.getOriginX(),this.getOriginY(),
				   tileW,
				   tileH,
				   this.getScaleX(),
				   this.getScaleY(), this.getRotation());
	}
	
	/**
	 * 
	 * Dessine la tête du personnage quand il regarde vers la gauche (un oeil, une bouche)
	 * 
	 * @param batch Batch 
	 * @param delta Temps ecoulé depuis le dernier dessin
	 */
	protected void drawLeft(SpriteBatch batch, float parentAlpha){
		
		float tileW = this.acharacter.bodyConfig.getEyesConfig().getEyesTileset().getTileWidth();
		float tileH = this.acharacter.bodyConfig.getEyesConfig().getEyesTileset().getTileHeight();
		float posx = this.acharacter.bodyConfig.getEyesConfig().getPosition().x;
		float posy = this.acharacter.bodyConfig.getEyesConfig().getPosition().y;
		Tileset tileset = this.acharacter.bodyConfig.getEyesConfig().getEyesTileset();
		TextureRegion eye;
		if (blink) eye=tileset.getTile(1);
		else eye=tileset.getTile(0);
		
		batch.draw(eye,
				   this.getX()- posx - tileW/2,
				   this.getY()+ posy - tileH/2,
				   this.getOriginX(),this.getOriginY(),
				   tileW,
				   tileH,
				   this.getScaleX(),
				   this.getScaleY(), this.getRotation());
		
		if (!blink)batch.draw(tileset.getTile(2),
				   this.getX()- posx - tileW/2 - this.acharacter.bodyConfig.getEyesConfig().getxRadius(),
				   this.getY()+ posy - tileH/2,
				   this.getOriginX(),this.getOriginY(),
				   tileW,
				   tileH,
				   this.getScaleX(),
				   this.getScaleY(), this.getRotation());
		
		posx = this.acharacter.bodyConfig.getMouthConfig().getPosition().x+6;
		posy = this.acharacter.bodyConfig.getMouthConfig().getPosition().y;
		tileW = this.acharacter.bodyConfig.getMouthConfig().getMouthTileset().getTileWidth();
		tileH = this.acharacter.bodyConfig.getMouthConfig().getMouthTileset().getTileHeight();
		batch.draw(this.acharacter.bodyConfig.getMouthConfig().getMouthTileset().getTile(1),
				   this.getX()+ posx - tileW/2 - this.acharacter.bodyConfig.getMouthConfig().getSidePosition().x,
				   this.getY()+ posy - tileH/2 + this.acharacter.bodyConfig.getMouthConfig().getSidePosition().y,
				   this.getOriginX(),this.getOriginY(),
				   tileW,
				   tileH,
				   this.getScaleX(),
				   this.getScaleY(), this.getRotation());
	}
	
	public void setBlink(boolean blink) {
		this.blink = blink;
	}

	public Vector2 getPupilPosition() {
		return pupilPosition;
	}

	public void setPupilPosition(Vector2 pupilPosition) {
		this.pupilPosition = pupilPosition;
	}
	
	
}
