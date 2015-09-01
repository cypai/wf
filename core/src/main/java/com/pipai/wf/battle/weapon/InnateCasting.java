package com.pipai.wf.battle.weapon;

public class InnateCasting extends SpellWeapon {

    @Override
    public boolean needsAmmunition() {
        return false;
    }

    @Override
    public int baseAmmoCapacity() {
        return 0;
    }

    @Override
    public String name() {
        return "Casting";
    }

}
