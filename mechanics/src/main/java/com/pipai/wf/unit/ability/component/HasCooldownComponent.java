package com.pipai.wf.unit.ability.component;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasCooldownComponent extends CooldownComponent {

	@JsonIgnore
	CooldownComponent getCooldownComponent();

	@Override
	default void startCooldown() {
		getCooldownComponent().startCooldown();
	}

	@Override
	default boolean onCooldown() {
		return getCooldownComponent().onCooldown();
	}

	@Override
	default int getCooldown() {
		return getCooldownComponent().getCooldown();
	}

	@Override
	default void setCooldown(int cooldown) {
		getCooldownComponent().setCooldown(cooldown);
	}

	@Override
	default void decrementCooldown() {
		getCooldownComponent().decrementCooldown();
	}

}
