package io.github.generaldeck;

public class LifeStealEffect implements OnHitEffect {
    private final float healPercentage;

    public LifeStealEffect(float percentage) {
        this.healPercentage = percentage;
    }

    @Override
    public void onHit(Unit attacker, Unit target) {
        float healAmount = attacker.damage * healPercentage;
        attacker.currentHp = Math.min(attacker.maxHp, attacker.currentHp + healAmount);
    }
}
