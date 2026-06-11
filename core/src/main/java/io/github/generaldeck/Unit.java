package io.github.generaldeck;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Unit implements Poolable {
    public UnitClass unitClass;
    public int team; // 0 para jogador, 1 para inimigo

    // atributos de combate
    public float maxHp;
    public float currentHp;
    public float damage;
    public float attackRange;
    public float moveSpeed;

    public float speedMultiplier = 1.0f;
    public float effectTimer = 0f;

    public final Vector2 position = new Vector2();
    public final Vector2 velocity = new Vector2();

    public boolean isDead;

    public UnitBehavior currentBehavior = UnitBehavior.SEEK_CLOSEST;

    public void init(UnitProfile profile, int team, float x, float y) {
        this.unitClass = profile.uClass;
        this.maxHp = profile.maxHp;
        this.currentHp = profile.maxHp;
        this.damage = profile.damage;
        this.attackRange = profile.range;
        this.moveSpeed = profile.speed;

        this.team = team;
        this.position.set(x, y);
        this.velocity.setZero();
        this.isDead = false;
    }

    public void setToArcher(int team, float x, float y) {
        this.init(UnitStats.ARCHER, team, x, y);
    }

    public void setToWarrior(int team, float x, float y) {
        this.init(UnitStats.WARRIOR, team, x, y);
    }

    @Override
    public void reset() {
        this.unitClass = null;
        this.position.setZero();
        this.velocity.setZero();
        this.currentHp = 0;
        this.isDead = true;
    }
}
