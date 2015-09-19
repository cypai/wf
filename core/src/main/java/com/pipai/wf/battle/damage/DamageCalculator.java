package com.pipai.wf.battle.damage;

import com.pipai.wf.util.UtilFunctions;

public class DamageCalculator {

	protected static boolean rollToHit(AccuracyPercentages a, int owPenalty) {
		int diceroll = 1 + UtilFunctions.rng.nextInt(100);
		return diceroll <= (int) (a.toHit() * Math.pow(0.7, owPenalty));
	}

	protected static boolean rollToCrit(AccuracyPercentages a) {
		int diceroll = 1 + UtilFunctions.rng.nextInt(100);
		return diceroll <= a.toCrit();
	}

	public static DamageResult rollDamageGeneral(AccuracyPercentages a, DamageFunction f, int owPenalty) {
		if (rollToHit(a, owPenalty)) {
			int dmg = 0;
			dmg = f.rollForDamage();
			boolean crit = rollToCrit(a);
			if (crit) {
				dmg *= 1.5;
			}
			return new DamageResult(true, crit, dmg, 0);
		} else {
			return DamageResult.missedResult();
		}
	}

	public static DamageResult rollDamageNormal(AccuracyPercentages a, DamageFunction f) {
		if (rollToHit(a, 0)) {
			int dmg = 0;
			dmg = f.rollForDamage();
			boolean crit = rollToCrit(a);
			if (crit) {
				dmg *= 1.5;
			}
			return new DamageResult(true, crit, dmg, 0);
		} else {
			return DamageResult.missedResult();
		}
	}

}
