package com.pipai.wf.battle.damage;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.util.Rng;

public class DamageCalculator {

	private final BattleConfiguration config;
	private final Rng rng;

	public DamageCalculator(BattleConfiguration config) {
		this.config = config;
		rng = config.getRng();
	}

	protected boolean rollToHit(AccuracyPercentages a, int owPenalty) {
		int diceroll = 1 + rng.nextInt(100);
		return diceroll <= (int) (a.toHit() * Math.pow(config.overwatchPenalty(), owPenalty));
	}

	protected boolean rollToCrit(AccuracyPercentages a) {
		int diceroll = 1 + rng.nextInt(100);
		return diceroll <= a.toCrit();
	}

	public DamageResult rollDamageGeneral(AccuracyPercentages a, DamageFunction f, int owPenalty) {
		if (rollToHit(a, owPenalty)) {
			int dmg = 0;
			dmg = f.rollForDamage(rng);
			boolean crit = rollToCrit(a);
			if (crit) {
				dmg *= config.critDamageMultiplier();
			}
			return new DamageResult(true, crit, dmg, 0);
		} else {
			return DamageResult.buildMissedResult();
		}
	}

	public DamageResult rollDamageNormal(AccuracyPercentages a, DamageFunction f) {
		if (rollToHit(a, 0)) {
			int dmg = 0;
			dmg = f.rollForDamage(rng);
			boolean crit = rollToCrit(a);
			if (crit) {
				dmg *= config.critDamageMultiplier();
			}
			return new DamageResult(true, crit, dmg, 0);
		} else {
			return DamageResult.buildMissedResult();
		}
	}

}
