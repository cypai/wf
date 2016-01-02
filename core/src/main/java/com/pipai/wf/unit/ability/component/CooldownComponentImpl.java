package com.pipai.wf.unit.ability.component;

public class CooldownComponentImpl implements CooldownComponent {

	private int maxCooldown;
	private int cooldown;

	public CooldownComponentImpl(int maxCooldown) {
		this.maxCooldown = maxCooldown;
	}

	@Override
	public void startCooldown() {
		cooldown = maxCooldown;
	}

	@Override
	public boolean onCooldown() {
		return cooldown > 0;
	}

	@Override
	public int getCooldown() {
		return cooldown;
	}

	@Override
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown < 0 ? 0 : cooldown;
	}

	@Override
	public void decrementCooldown() {
		cooldown = cooldown > 0 ? cooldown - 1 : 0;
	}

}
