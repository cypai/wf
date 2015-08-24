package com.pipai.wf.battle.agent;

import java.util.ArrayList;
import java.util.LinkedList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.LeatherArmor;
import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.log.BattleEventLoggable;
import com.pipai.wf.battle.log.BattleLog;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.CoverType;
import com.pipai.wf.battle.map.Direction;
import com.pipai.wf.battle.map.DirectionalCoverSystem;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.config.WFConfig;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.util.UtilFunctions;

public class Agent implements BattleEventLoggable {
	
	public enum State {NEUTRAL, KO, OVERWATCH};
	
	protected Team team;
	protected int maxHP, maxAP, maxMP, hp, ap, mp;
	protected int mobility, aim, defense;
	protected State state;
	protected ArrayList<Weapon> weapons;
	protected int weaponIndex;
	protected Armor armor;
	protected BattleMap map;
	protected GridPosition position;
	protected BattleLog log;
	protected Attack overwatchAttack;
	
	public Agent(BattleMap map, AgentState state) {
		this.map = map;
		team = state.team;
		position = state.position;
		maxHP = state.maxHP;
		hp = state.hp;
		maxAP = state.maxAP;
		mp = state.mp;
		maxMP = state.maxMP;
		ap = state.ap;
		mobility = state.mobility;
		aim = 60;
		defense = 0;
		weapons = new ArrayList<Weapon>();
		weapons.add(new Pistol());
		weapons.add(new SpellWeapon());
		weaponIndex = 0;
		armor = new LeatherArmor();
	}
	
	public Team getTeam() { return this.team; }
	public void setTeam(Team team) { this.team = team; }
	public int getAP() { return this.ap; }
	public void setAP(int ap) { this.ap = ap; }
	public int getMaxAP() { return this.maxAP; }
	public int getHP() { return this.hp; }
	public void setHP(int hp) {
		this.hp = hp;
		if (this.hp <= 0) {
			this.hp = 0;
			this.state = State.KO;
			this.map.getCell(this.position).makeAgentInactive();
		} else {
			this.state = State.NEUTRAL;
		}
	}
	public void takeDamage(int amt) {
		int pierceDmg = this.armor.takeDamage(amt);
		this.setHP(this.getHP() - pierceDmg);
	}
	public int getMaxHP() { return this.maxHP; }
	public int getMP() { return this.mp; }
	public void setMP(int mp) { this.mp = mp; }
	public void useMP(int mp) { this.mp -= mp; }
	public int getMaxMP() { return this.maxMP; }
	public int getMobility() { return this.mobility; }
	public int getBaseAim() { return this.aim; }
	public Weapon getCurrentWeapon() { return this.weapons.get(this.weaponIndex); }
	public Armor getArmor() { return this.armor; }
	public boolean isKO() { return this.state == State.KO; }
	public boolean isOverwatching() { return this.state == State.OVERWATCH; }

	@SuppressWarnings("unchecked")
	public ArrayList<Weapon> getWeapons() { return (ArrayList<Weapon>) this.weapons.clone(); }
	
	public GridPosition getPosition() { return this.position; }
	
	private void setPosition(GridPosition pos) {
		this.map.getCell(this.position).removeAgent();
		this.map.getCell(pos).setAgent(this);
		this.position = pos;
	}
	
	public ArrayList<GridPosition> getPeekingSquares() {
		ArrayList<GridPosition> l = new ArrayList<GridPosition>();
		l.add(this.getPosition());
		ArrayList<Direction> coverDirs = DirectionalCoverSystem.getCoverDirections(this.map, this.getPosition());
		for (Direction coverDir : coverDirs) {
			ArrayList<Direction> perpendicularDirs = Direction.getPerpendicular(coverDir);
			for (Direction perpendicular : perpendicularDirs) {
				BattleMapCell peekSquare = this.map.getCellInDirection(this.getPosition(), perpendicular);
				if (peekSquare != null) {
					GridPosition pos = peekSquare.getPosition();
					BattleMapCell peekCoverSquare = this.map.getCellInDirection(pos, coverDir);
					
					if (peekSquare.isEmpty() && !peekCoverSquare.hasTileSightBlocker() && !l.contains(pos)) {
						l.add(peekSquare.getPosition());
					}
				}
			}
		}
		return l;
	}
	
	public CoverType getCoverType() {
		return DirectionalCoverSystem.getCover(this.map, this.getPosition());
	}
	
	public boolean isOpen() {
		return DirectionalCoverSystem.isOpen(map, this.getPosition());
	}
	
	public boolean isFlanked() {
		for (Agent a : this.enemiesInRange()) {
			if (this.isFlankedBy(a)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isFlankedBy(Agent other) {
		ArrayList<GridPosition> otherPosList = other.getPeekingSquares();
		for (GridPosition otherPos : otherPosList) {
			if (DirectionalCoverSystem.isFlankedBy(map, this.getPosition(), otherPos)) {
				return true;
			}
		}
		return false;
	}
	
	public int getDefense(GridPosition attackerPos) {
		int situationalDef = this.defense + DirectionalCoverSystem.getBestCoverAgainstAttack(map, this.getPosition(), attackerPos).getDefense();
		return situationalDef;
	}
	
	public boolean canSee(Agent other) {
		for (GridPosition peekSquare : this.getPeekingSquares()) {
			for (GridPosition otherPeekSquare : other.getPeekingSquares()) {
				if (UtilFunctions.gridPositionDistance(peekSquare, otherPeekSquare) < WFConfig.battleProps().sightRange()) { 
					if (this.map.lineOfSight(peekSquare, otherPeekSquare)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean inRange(Agent other) {
		return other.canSee(this);
	}
	
	public ArrayList<Agent> enemiesInRange() {
		ArrayList<Agent> l = new ArrayList<Agent>();
		for (Agent a : this.map.getAgents()) {
			if (!a.isKO() && a.team != this.team && this.canSee(a)) {
				l.add(a);
			}
		}
		return l;
	}
	
	public ArrayList<Agent> targetableEnemies() {
		ArrayList<Agent> list = new ArrayList<Agent>();
		if (this.getCurrentWeapon().currentAmmo() == 0) {
			return list;
		} else {
			return this.enemiesInRange();
		}
	}
	
	public void postTurnReset() {
		ap = maxAP;
		if (!isKO()) {
			state = State.NEUTRAL;
		}
	}
	
	public void switchWeapon() {
		weaponIndex += 1;
		if (weaponIndex == weapons.size()) {
			weaponIndex = 0;
		}
		logEvent(BattleEvent.switchWeaponEvent(this));
	}
	
	public void move(LinkedList<GridPosition> path, int useAP) throws IllegalActionException {
		if (useAP > this.ap) {
			throw new IllegalActionException("AP required for movement greater than current AP");
		}
		boolean isValid = true;
		BattleEvent event = BattleEvent.moveEvent(this, path);
		for (GridPosition pos : path) {
			if (pos.equals(this.getPosition())) { continue; }
			BattleMapCell cell = this.map.getCell(pos);
			if (cell == null || !cell.isEmpty()) {
				isValid = false;
				break;
			}
		}
		if (isValid) {
			logEvent(event);
		} else {
			throw new IllegalActionException("Move path sequence is not valid");
		}
		for (GridPosition pos : path) {
			this.setPosition(pos);
			for (Agent a : this.enemiesInRange()) {
				if (a.isOverwatching()) {
					a.activateOverwatch(this, event, pos);
					if (this.isKO()) {
						return;
					}
				}
			}
		}
		GridPosition dest = path.peekLast();
		this.setPosition(dest);
		this.setAP(this.ap - useAP);
	}
	
	public void rangeAttack(Agent other, Attack attack) {
		this.getCurrentWeapon().expendAmmo(attack.requiredAmmo());
		float distance = 0;
		boolean hit = attack.rollToHit(this, other, distance);
		int dmg = 0;
		if (hit) {
			dmg = attack.damageRoll(this, other, distance);
			other.takeDamage(dmg);
		}
		this.setAP(0);
		logEvent(BattleEvent.attackEvent(this, other, attack, hit, dmg));
	}
	
	public void overwatch(Attack attack) {
		this.overwatchAttack = attack;
		this.state = State.OVERWATCH;
		this.setAP(0);
		logEvent(BattleEvent.overwatchEvent(this, attack));
	}
	
	public void activateOverwatch(Agent other, BattleEvent activationLogEvent, GridPosition activatedTile) {
		float distance = 0;
		boolean hit = overwatchAttack.rollToHit(this, other, distance);
		this.state = State.NEUTRAL;
		int dmg = 0;
		if (hit) {
			dmg = overwatchAttack.damageRoll(this, other, distance);
			other.takeDamage(dmg);
		}
		activationLogEvent.addChainEvent(BattleEvent.overwatchActivationEvent(this, other, overwatchAttack, activatedTile, hit, dmg));
	}
	
	public void reload() {
		this.getCurrentWeapon().reload();
		this.setAP(0);
		logEvent(BattleEvent.reloadEvent(this));
	}
	
	public void readySpell(Spell spell) throws IllegalActionException {
		if (this.mp < spell.requiredMP()) {
			throw new IllegalActionException("Not enough mp to cast " + spell.name());
		}
		if (!(this.getCurrentWeapon() instanceof SpellWeapon)) {
			throw new IllegalActionException("Currently selected weapon is not a spell weapon");
		}
		this.useMP(spell.requiredMP());
		this.setAP(this.ap - 1);
		((SpellWeapon)this.getCurrentWeapon()).ready(spell);
		logEvent(BattleEvent.readySpellEvent(this, spell));
	}
	
	public void castReadiedSpell(Agent other) throws IllegalActionException {
		if (!(this.getCurrentWeapon() instanceof SpellWeapon)) {
			throw new IllegalActionException("Currently selected weapon is not a spell weapon");
		}
		Spell readiedSpell = ((SpellWeapon)this.getCurrentWeapon()).getSpell();
		if (readiedSpell == null) {
			throw new IllegalActionException("No readied spell available");
		}
		if (!readiedSpell.canTargetAgent()) {
			throw new IllegalActionException("Cannot target with " + readiedSpell.name());
		}
		float distance = 0;
		boolean hit = readiedSpell.rollToHit(this, other, distance);
		int dmg = 0;
		if (hit) {
			dmg = readiedSpell.damageRoll();
			other.takeDamage(dmg);
		}
		this.setAP(0);
		logEvent(BattleEvent.castTargetEvent(this, other, readiedSpell, hit, dmg));
	}
	
	@Override
	public void register(BattleLog log) {
		this.log = log;
	}
	
	private void logEvent(BattleEvent ev) {
		if (log != null) {
			log.logEvent(ev);
		}
	}
	
}
