package com.pipai.wf.battle.action;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.exception.IllegalActionException;

public class ReadySpellAction extends AlterStateAction {

	private Spell spell;

	public ReadySpellAction(BattleController controller, Agent performerAgent, Spell spell) {
		super(controller, performerAgent);
		this.spell = spell;
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent a = getPerformer();
		if (!a.getAbilities().hasSpell(spell)) {
			throw new IllegalActionException("Does not have the ability to cast " + spell.getName());
		}
		if (a.getMP() < spell.requiredMP()) {
			throw new IllegalActionException("Not enough mp to cast " + spell.getName());
		}
		if (!(a.getCurrentWeapon() instanceof SpellWeapon)) {
			throw new IllegalActionException("Currently selected weapon is not a spell weapon");
		}
		int actLevel = a.getAbilities().getActualizationLevel(spell.element());
		if (actLevel == 0) {
			throw new IllegalActionException("Does not have required actualization ability");
		}
		a.useMP(spell.requiredMP());
		boolean quicken = true;
		if (actLevel <= spell.requiredMP()) {
			a.setAP(a.getAP() - 1);
			quicken = false;
		}
		((SpellWeapon) a.getCurrentWeapon()).ready(spell);
		logBattleEvent(BattleEvent.readySpellEvent(a, spell, quicken));
	}

	@Override
	public String getName() {
		return "Ready: " + spell.getName();
	}

	@Override
	public String getDescription() {
		return spell.getDescription();
	}

}
