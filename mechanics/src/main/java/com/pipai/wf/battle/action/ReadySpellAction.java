package com.pipai.wf.battle.action;

import java.util.Arrays;
import java.util.List;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.ApRequiredComponent;
import com.pipai.wf.battle.action.component.HasWeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponent;
import com.pipai.wf.battle.action.component.WeaponComponentImpl;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.action.verification.BaseVerifier;
import com.pipai.wf.battle.action.verification.HasSpellVerifier;
import com.pipai.wf.battle.action.verification.PredicateVerifier;
import com.pipai.wf.battle.action.verification.SpellMpVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.ReadySpellEvent;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.SpellWeapon;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.spell.Spell;

public class ReadySpellAction extends PerformerAction implements ApRequiredComponent, HasWeaponComponent {

	private WeaponComponent weaponComponent = new WeaponComponentImpl();

	private Spell spell;

	public ReadySpellAction(BattleController controller, Agent performerAgent, SpellWeapon weapon) {
		super(controller);
		setPerformer(performerAgent);
	}

	public ReadySpellAction(BattleController controller, Agent performerAgent, SpellWeapon weapon, Spell spell) {
		super(controller);
		setWeapon(weapon);
		setPerformer(performerAgent);
		this.spell = spell;
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	public WeaponComponent getWeaponComponent() {
		return weaponComponent;
	}

	@Override
	protected List<ActionVerifier> getVerifiers() {
		return Arrays.asList(BaseVerifier.getInstance(),
				new HasSpellVerifier(getPerformer(), spell),
				new SpellMpVerifier(getPerformer(), spell),
				new PredicateVerifier<Weapon>(weapon -> weapon instanceof SpellWeapon, getWeapon(),
						"Weapon is not a spell weapon"));
	}

	@Override
	protected BattleEvent performImpl() throws IllegalActionException {
		Agent a = getPerformer();
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
		((SpellWeapon) getWeapon()).setSpell(spell);
		return new ReadySpellEvent(a, spell, quicken);
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
