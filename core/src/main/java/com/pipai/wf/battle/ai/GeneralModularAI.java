package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.ReadySpellAction;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyAction;
import com.pipai.wf.battle.action.WeaponActionFactory;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.DirectionalCoverSystem;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;

public class GeneralModularAI extends ModularAI {

	private ArrayList<Agent> playerAgents;
	private MapGraph mapgraph;

	public GeneralModularAI(BattleMap map, Agent a) {
		super(map, a);
		mapgraph = new MapGraph(getBattleMap(), getAgent().getPosition(), getAgent().getMobility(), 1, 2);
		playerAgents = getAgentsInTeam(Team.PLAYER);
	}

	@Override
	public ActionScore getBestMove() {
		Agent a = getAgent();
		ActionScore best;
		if (a.isFlanked()) {
			best = getBestMoveAction();
		} else {
			best = getBestAttackAction();
		}
		if (best.action == null) {
			return new ActionScore(reloadWeaponAction(), 20);
		}
		return best;
	}

	private Action reloadWeaponAction() {
		if (getAgent().getCurrentWeapon() instanceof SpellWeapon) {
			return new ReadySpellAction(getAgent(), new FireballSpell());
		} else {
			return new ReloadAction(getAgent());
		}
	}

	private ActionScore getBestAttackAction() {
		Agent a = getAgent();
		Weapon w = a.getCurrentWeapon();
		if (a.enemiesInRange().size() == 0) {
			if (w instanceof SpellWeapon) {
				SpellWeapon sw = (SpellWeapon) w;
				if (sw.getSpell() == null) {
					return new ActionScore(new ReadySpellAction(a, new FireballSpell()), 20);
				} else {
					return new ActionScore(new OverwatchAction(a), 30);
				}
			} else {
				if (!w.needsAmmunition() || (w.needsAmmunition() && w.currentAmmo() > 0)) {
					return new ActionScore(new OverwatchAction(a), 30);
				} else {
					return new ActionScore(new ReloadAction(a), 20);
				}
			}
		} else {
			if (w.needsAmmunition() && w.currentAmmo() == 0) {
				return new ActionScore(new ReloadAction(a), 20);
			} else {
				if (w instanceof SpellWeapon) {
					SpellWeapon sw = (SpellWeapon) w;
					if (sw.getSpell() == null) {
						return new ActionScore(new ReadySpellAction(a, new FireballSpell()), 20);
					}
				}
				ActionScore best = new ActionScore(null, Float.MIN_NORMAL);
				for (Agent player : playerAgents) {
					if (player.isKO()) {
						continue;
					}
					TargetedWithAccuracyAction action = WeaponActionFactory.defaultWeaponAction(a, player);
					best = best.compareAndReturnBetter(new ActionScore(action, action.toHit()));
				}
				return best;
			}
		}
	}

	private ActionScore getBestMoveAction() {
		ArrayList<GridPosition> potentialTiles = mapgraph.getMovableCellPositions(1);
		ActionScore best = new ActionScore(null, Float.MIN_NORMAL);
		for (GridPosition pos : potentialTiles) {
			float score = scorePosition(pos);
			best = best.compareAndReturnBetter(new ActionScore(new MoveAction(getAgent(), mapgraph.getPath(pos), 1), score));
		}
		return best;
	}

	private float scorePosition(GridPosition pos) {
		BattleMap map = getBattleMap();
		float min = Float.MAX_VALUE;
		for (Agent a : playerAgents) {
			if (a.isKO()) {
				continue;
			}
			float current = DirectionalCoverSystem.getBestCoverAgainstAttack(map, pos, a.getPosition()).getDefense();
			min = (current < min) ? current : min;
		}
		return min;
	}

}
