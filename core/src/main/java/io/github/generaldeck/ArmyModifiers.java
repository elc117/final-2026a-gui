package io.github.generaldeck;

import com.badlogic.gdx.utils.Array;

public class ArmyModifiers {
    // Modificadores de estatistica
    public float warriorBonusDamage = 0f;
    public float warriorBonusHealth = 0f;

    public int archerBonusPierce = 0;
    public float archerBonusDamage = 0f;

    // Lista de modificadores de comportamento
    public Array<OnHitEffect> warriorOnHitEffects = new Array<>();
    public Array<OnHitEffect> archerOnHitEffects = new Array<>();
    public Array<OnHitEffect> monkOnHitEffects = new Array<>();

    public void reset() {
        warriorBonusDamage = 0f;
        warriorBonusHealth = 0f;
        warriorOnHitEffects.clear();
        archerOnHitEffects.clear();
        monkOnHitEffects.clear();
        archerBonusPierce = 0;
        archerBonusDamage = 0;
    }
}
