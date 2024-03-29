package enibdevlab.dwarves.controllers.actions;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import enibdevlab.dwarves.DwarvesManager;
import enibdevlab.dwarves.controllers.actions.animations.Animation;
import enibdevlab.dwarves.models.Difficulty;
import enibdevlab.dwarves.models.Direction;
import enibdevlab.dwarves.models.Game;
import enibdevlab.dwarves.models.characters.Complaint;
import enibdevlab.dwarves.models.characters.Miner;
import enibdevlab.dwarves.models.items.Pickaxe;
import enibdevlab.dwarves.views.actors.ADiamondEffect;
import enibdevlab.dwarves.views.actors.ParticleActor;
import enibdevlab.dwarves.views.audio.SoundManager;
import enibdevlab.dwarves.views.world.Tile;

public class Mining extends DwarfAction {
	
	/**
	 * Temps par d�faut pour effectuer l'action (multipli� par la duret� du bloc)
	 */
	private static float MINING_TIME = .7f;
	
	/**
	 * Progression du minage
	 */
	protected int progression;

	/**
	 * Position tile � miner
	 */
	protected Vector2 blockPosition;
	
	/**
	 * Vrai Tile
	 */
	protected Tile toMine;
	
	/**
	 * Mineur qui mine
	 */
	protected Miner miner;
	
	/**
	 * R�ussite ou pas
	 */
	protected boolean success;
	
	
	/**
	 * Action de miner
	 * @param game
	 * @param dwarf
	 * @param tile
	 */
	public Mining(Game game, Miner dwarf, Vector2 tile) {
		super(game, dwarf);
		this.blockPosition = tile;
		this.miner = dwarf;
		this.success = false;
		this.progression = 0;
		this.toMine = game.getLevel().getTilemap().getTile(0, (int)miner.getToMine().x, (int)miner.getToMine().y);
	}

	@Override
	public void entry() {
		
		progression = 0;
		
		// On v�rifie qu'on a une pioche en �tat
		boolean ok = false;
		if(dwarf.getRightHandItem() != null){
			if(dwarf.getRightHandItem() instanceof Pickaxe){
				ok = true;
			}
		}
		if(!ok){
			abort(Complaint.TROLL);
			finished = true;
			return;
		}
		
		// Tourne face au bloc
		// Met le personnage dans la bonne direction
		if(toMine.getY()>dwarf.getY()) dwarf.getView().setDirection(Direction.TOP);
		else if (toMine.getY()<dwarf.getY()) dwarf.getView().setDirection(Direction.BOTTOM);
		else if(toMine.getX()>dwarf.getX()) dwarf.getView().setDirection(Direction.RIGHT);
		else if (toMine.getX()<dwarf.getX()) dwarf.getView().setDirection(Direction.LEFT);
		
		
		
	}

	@Override
	public void doAction(float delta) {
		
		float power = ((Pickaxe) miner.getRightHandItem()).getPower();
		
		if(progression > this.toMine.getTileProperty("hardness")*(MINING_TIME/power)){ // MINING_TIME secondes pour miner un bloc
			int sound = DwarvesManager.random.nextInt(4);
			SoundManager.play("mined" + Integer.toString(sound));
			((Pickaxe)(this.dwarf.getRightHandItem())).onUse();
			
			// On regarde sion a pas cass� la pioche
			if(((Pickaxe)(this.dwarf.getRightHandItem())).getDamage()>=100 && Difficulty.isPickaxeBreakable()){
				pickaxeBroke();
			}
			
			finished=true;
			success = true;
		}
		else{
			// Planifie l'animation
			dwarf.getView().addAction(Animation.mining(dwarf.getView(), MINING_TIME/power));
			// On regarde si on a pas cass� la pioche
			if(((Pickaxe)(this.dwarf.getRightHandItem())).getDamage()>=100){
				pickaxeBroke();
				finished = true;
				success  = false;
			}
		}
		
		progression += 1;
		doWaiting(MINING_TIME/power);
	}

	@Override
	public void finish() {
		if(success){
			this.game.getBank().addMoney(this.toMine.getTileProperty("wealth"));
			if(this.toMine.getTileProperty("wealth")>0){
				SoundManager.play("cash");
				this.game.getView().getGameplayLayer().priceEffect((int)(toMine.getX()*game.getLevel().getTileXSize()),
				(int)(toMine.getY()*game.getLevel().getTileYSize()), this.toMine.getTileProperty("wealth"));
			}
			if(this.toMine.getTileProperty("diamond")>0){
				this.game.getBank().addDiamond();
				this.game.getView().addActor(new ADiamondEffect());
			}
			miner.tileMined();
			
			// Effets de particules pour la hype
			if(!(Gdx.app.getType()==ApplicationType.Android)){
				int tw = Game.getInstance().getView().getGameplayLayer().getTilemap().getTileWidth();
				int th = Game.getInstance().getView().getGameplayLayer().getTilemap().getTileHeight();
				Game.getInstance().getView().getGameplayLayer().addActor(new ParticleActor(toMine.getX()*tw+tw/2f, toMine.getY()*th+th/2f, "dirt"));
			}
		}
		else{
			
		}
	}

	
	public void pickaxeBroke(){
		this.dwarf.setRightHandItem(null);
		SoundManager.play("break");
		game.fireDwarfEvent("pickaxeBreak",String.valueOf(this.dwarf.getPosition().x),String.valueOf(this.dwarf.getPosition().y));
	}
}
