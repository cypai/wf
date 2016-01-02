package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.unit.ability.component.HasLevelledAbilityComponent;
import com.pipai.wf.unit.ability.component.LevelledAbilityComponent;
import com.pipai.wf.unit.ability.component.LevelledAbilityImpl;
import com.pipai.wf.unit.ability.component.RoundEndComponent;

public class RegenerationAbility extends Ability implements RoundEndComponent, HasLevelledAbilityComponent {

	private LevelledAbilityComponent levelledAbilityImpl = new LevelledAbilityImpl();

	public RegenerationAbility() {
		setLevel(1);
	}

	public RegenerationAbility(int level) {
		setLevel(level);
	}

	@Override
	public LevelledAbilityComponent getLevelledAbilityComponent() {
		return levelledAbilityImpl;
	}

	@Override
	public String getName() {
		return "Regeneration";
	}

	@Override
	public String getDescription() {
		return "Regenerates a set amount of HP per turn";
	}

	@Override
	public Ability clone() {
		return new RegenerationAbility(getLevel());
	}

	@Override
	public void onRoundEnd(Agent agent) {
		agent.heal(getLevel());
	}

}
