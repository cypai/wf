package com.pipai.wf.battle.action;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.action.component.DefaultApRequiredComponent;
import com.pipai.wf.battle.action.component.HasPerformerComponent;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.action.verification.BaseVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.exception.IllegalActionException;

public class EquipArmorAction extends PerformerAction implements DefaultApRequiredComponent, HasPerformerComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(EquipArmorAction.class);

	private int slot;

	public void setSlot(int slot) {
		this.slot = slot;
	}

	@Override
	protected List<ActionVerifier> getVerifiers() {
		return Arrays.asList(
				BaseVerifier.getInstance());
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		Agent performer = getPerformer();
		LOGGER.debug("Performed by '" + performer.getName() + "' equipping " + performer.getInventory().getItemName(slot) + " at slot " + slot);
		performer.getInventory().equipArmor(slot);
		performer.setAP(0);
		// logBattleEvent(event);
	}

	@Override
	public String getName() {
		return "Equip Armor";
	}

	@Override
	public String getDescription() {
		return "Equip the selected armor";
	}

}
