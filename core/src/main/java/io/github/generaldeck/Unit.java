package io.github.gameprototipo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Unit implements Poolable {
    public enum UnitClass { MELEE, RANGED, MAGIC }
    public UnitClass unitClass;
    public int team; // 0 para jogador, 1 para inimigo

    // atributos de combate
    public float maxHp;
    public float currentHp;
    public float damage;
    public float attackRange;
    public float moveSpeed;

    public final Vector2 position = new Vector2();
    public final Vector2 velocity = new Vector2();

    public boolean isDead;

    public void init(UnitClass uClass, int team, float x, float y, float maxHp, float damage, float range, float speed) {
        this.unitClass = uClass;
        this.team = team;
        this.position.set(x, y);
        this.velocity.setZero();

        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.damage = damage;
        this.attackRange = range;
        this.moveSpeed = speed;

        this.isDead = false;
    }

    public void setToArcher(float x, float y) {
        this.init(UnitClass.RANGED, 0, x, y,
            UnitStats.ARCHER_HP,
            UnitStats.ARCHER_DAMAGE,
            UnitStats.ARCHER_RANGE,
            UnitStats.ARCHER_SPEED);
    }

    public void setToWarrior(float x, float y) {
        this.init(UnitClass.MELEE, 0, x, y,
            UnitStats.WARRIOR_HP,
            UnitStats.WARRIOR_DAMAGE,
            UnitStats.WARRIOR_RANGE,
            UnitStats.WARRIOR_SPEED);
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
