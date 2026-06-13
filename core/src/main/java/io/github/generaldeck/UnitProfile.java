package io.github.generaldeck;

public class UnitProfile {
    public final UnitClass uClass;
    public final float maxHp;
    public final float damage;
    public final float range;
    public final float speed;
    public final float attackCooldown;

    public UnitProfile(UnitClass uClass, float maxHp, float damage, float range, float speed, float attackCooldown) {
        this.uClass = uClass;
        this.maxHp = maxHp;
        this.damage = damage;
        this.range = range;
        this.speed = speed;
        this.attackCooldown = attackCooldown;
    }
}
