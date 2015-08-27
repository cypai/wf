package com.pipai.wf.unit.ability;

public class AbilityFactory {
	
	public static Ability clone(Ability a) {
		return createAbility(a.type());
	}
	
	public static Ability createAbility(AbilityType t) {
		switch (t) {
		case REGENERATION:
			return new RegenerationAbility(1);
		case FIREBALL:
			return new FireballAbility();
		default:
			return null;
		}
	}
	
}