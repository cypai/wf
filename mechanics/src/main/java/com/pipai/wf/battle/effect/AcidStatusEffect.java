package com.pipai.wf.battle.effect;

import java.util.ArrayList;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageDealer;
import com.pipai.wf.battle.damage.DamageResult;

public class AcidStatusEffect extends StatusEffect {

	private static final ArrayList<Class<? extends Action>> ACTION_WHITELIST = new ArrayList<>(1);

	// static {
	// ACTION_WHITELIST.add(SwitchWeaponAction.class);
	// }

	private DamageDealer damageDealer;

	public AcidStatusEffect(Agent agent, int cooldown, DamageDealer damageDealer) {
		super(agent, cooldown);
		this.damageDealer = damageDealer;
	}

	@Override
	public int flatAimModifier() {
		return -30;
	}

	@Override
	public int flatMobilityModifier() {
		return -getAgent().getMobility() / 2;
	}

	@Override
	public String name() {
		return "Acid";
	}

	@Override
	public String description() {
		return "Restricts movement and decreases aim. Upon any costly action take 1 damage.";
	}

	@Override
	protected void onActionImpl(Action action) {
		for (Class<? extends Action> klass : ACTION_WHITELIST) {
			if (klass.isInstance(action)) {
				// Passes whitelist, don't take damage
				return;
			}
		}
		// Did not pass, take damage
		damageDealer.doDamage(new DamageResult(true, false, 1, 0), getAgent());
	}

	@Override
	public AcidStatusEffect copy() {
		return new AcidStatusEffect(getAgent(), getCooldown(), damageDealer);
	}

}
