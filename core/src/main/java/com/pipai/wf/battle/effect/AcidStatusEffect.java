package com.pipai.wf.battle.effect;

import java.util.ArrayList;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.SwitchWeaponAction;
import com.pipai.wf.battle.agent.Agent;

public class AcidStatusEffect extends StatusEffect {

	private static final ArrayList<Class<? extends Action>> actionWhitelist = new ArrayList<Class<? extends Action>>();

	static {
		actionWhitelist.add(SwitchWeaponAction.class);
	}

	public AcidStatusEffect(Agent agent, int cooldown) {
		super(agent, cooldown);
	}

	@Override
	public int flatAimModifier() {
		return -30;
	}

	@Override
	public int flatMobilityModifier() {
		return -getAgent().getBaseMobility()/2;
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
		for (Class<? extends Action> klass : actionWhitelist) {
			if (klass.isInstance(action)) {
				// Passes whitelist, don't take damage
				return;
			}
		}
		// Did not pass, take damage
		getAgent().takeDamage(1);
	}

}