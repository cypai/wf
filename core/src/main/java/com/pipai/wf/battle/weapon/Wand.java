package com.pipai.wf.battle.weapon;

import com.pipai.wf.battle.spell.Spell;

public class Wand extends SpellWeapon {

    @Override
    public boolean needsAmmunition() {
        return true;
    }

    @Override
    public int baseAmmoCapacity() {
        return 1;
    }

    @Override
    public void ready(Spell spell) {
        super.ready(spell);
        this.reload();
    }

    @Override
    public void cast() {
        super.cast();
        this.expendAmmo(1);
    }

    @Override
    public String name() {
        return "Wand";
    }

}
