package com.pipai.wf.battle.agent;

import java.util.ArrayList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.log.BattleEventLoggable;
import com.pipai.wf.battle.log.BattleLog;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.CoverType;
import com.pipai.wf.battle.map.Direction;
import com.pipai.wf.battle.map.DirectionalCoverSystem;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.misc.OverwatchContainer;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.config.WFConfig;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;
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
	protected OverwatchContainer owContainer;
	protected AbilityList abilities;

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
		aim = state.aim;
		defense = state.defense;
		weapons = state.weapons;
		weaponIndex = 0;
		armor = state.armor;
		abilities = state.abilities.clone();
		owContainer = new OverwatchContainer();
	}

	public BattleMap getBattleMap() { return this.map; }
	public Team getTeam() { return this.team; }
	public void setTeam(Team team) { this.team = team; }
	public int getAP() { return this.ap; }
	public void setAP(int ap) { this.ap = ap; }
	public void useAP(int ap) { this.ap -= ap; if (this.ap < 0) {
		this.ap = 0;
	} }
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
	public void heal(int amt) {
		this.hp += amt;
		if (this.hp > this.maxHP) {
			this.hp = this.maxHP;
		}
	}
	public int getMaxHP() { return this.maxHP; }
	public int getMP() { return this.mp; }
	public void setMP(int mp) { this.mp = mp; }
	public void useMP(int mp) { this.mp -= mp; }
	public int getMaxMP() { return this.maxMP; }
	public int getMobility() { return this.mobility; }
	public int getBaseAim() { return this.aim; }
	public Weapon getCurrentWeapon() {
		if (this.weapons.size() == 0) {
			return null;
		}
		return this.weapons.get(this.weaponIndex);
	}
	public Armor getArmor() { return this.armor; }
	public boolean isKO() { return this.state == State.KO; }
	public boolean isOverwatching() { return this.state == State.OVERWATCH; }
	public AbilityList getAbilities() { return this.abilities; }

	@SuppressWarnings("unchecked")
	public ArrayList<Weapon> getWeapons() { return (ArrayList<Weapon>) this.weapons.clone(); }

	public ArrayList<Spell> getSpellList() {
		ArrayList<Spell> l = new ArrayList<Spell>();
		for (Ability a : abilities) {
			if (a.grantsSpell()) {
				l.add(a.getGrantedSpell());
			}
		}
		return l;
	}

	public GridPosition getPosition() { return this.position; }

	public void setPosition(GridPosition pos) {
		this.map.getCell(this.position).removeAgent();
		this.map.getCell(pos).setAgent(this);
		this.position = pos;
	}

	public float getDistanceFrom(Agent other) {
		return (float) getPosition().distance(other.getPosition());
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

	public void procRoundEndAbilities() {
		for (Ability a : abilities) {
			a.notifyRoundEnd(this);
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

	public void overwatch(Class<? extends TargetedWithAccuracyAction> attack) {
		this.owContainer.prepareAction(attack);
		this.state = State.OVERWATCH;
		this.setAP(0);
	}

	public void activateOverwatch(Agent other, BattleEvent activationLogEvent, GridPosition activatedTile) {
		TargetedWithAccuracyAction action = this.owContainer.generateAction(this, other);
		try {
			other.setPosition(activatedTile);
			action.performOnOverwatch(activationLogEvent);
			this.owContainer.clear();
			this.state = State.NEUTRAL;
		} catch (IllegalActionException e) {
			System.out.println(e.getMessage());
		}
	}

	public void reload() {
		this.getCurrentWeapon().reload();
		this.setAP(0);
		logEvent(BattleEvent.reloadEvent(this));
	}

	public void readySpell(Spell spell) throws IllegalActionException {
		if (!this.abilities.hasSpell(spell)) {
			throw new IllegalActionException("Does not have the ability to cast " + spell.name());
		}
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
