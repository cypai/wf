package com.pipai.wf.unit.ability.component;

public interface HasCooldownComponent extends CooldownComponent {

	CooldownImpl getCooldownImpl();

	@Override
	default void startCooldown() {
		getCooldownImpl().startCooldown();
	}

	@Override
	default boolean onCooldown() {
		return getCooldownImpl().onCooldown();
	}

	@Override
	default int getCooldown() {
		return getCooldownImpl().getCooldown();
	}

	@Override
	default void setCooldown(int cooldown) {
		getCooldownImpl().setCooldown(cooldown);
	}

	@Override
	default void decrementCooldown() {
		getCooldownImpl().decrementCooldown();
	}

}
