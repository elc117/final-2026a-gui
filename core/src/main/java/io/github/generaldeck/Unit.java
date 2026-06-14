package io.github.generaldeck;

import com.badlogic.gdx.math.Vector2;

public class Unit {
    public String type; // "WARRIOR" ou "ARCHER"
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public int team;
    public float currentHp, maxHp;
    public float damage;
    public float moveSpeed;
    public float attackRange;
    public float attackCooldown;
    public float attackTimer;
    public float effectTimer;
    public float speedMultiplier = 1.0f;
    public boolean isDead;
    public UnitBehavior currentBehavior = UnitBehavior.SEEK_CLOSEST;

    public void setToWarrior(int team, float x, float y) {
        this.type           = "WARRIOR";
        this.team           = team;
        this.position.set(x, y);
        this.velocity.setZero();
        this.maxHp          = 100f;
        this.currentHp      = maxHp;
        this.damage         = 10f;
        this.moveSpeed      = 60f;
        this.attackRange    = 30f;
        this.attackCooldown = 1.0f;
        this.attackTimer    = 0f;
        this.effectTimer    = 0f;
        this.speedMultiplier = 1.0f;
        this.isDead         = false;
        this.currentBehavior = UnitBehavior.SEEK_CLOSEST;
    }

    public void setToArcher(int team, float x, float y) {
        this.type           = "ARCHER";
        this.team           = team;
        this.position.set(x, y);
        this.velocity.setZero();
        this.maxHp          = 70f;
        this.currentHp      = maxHp;
        this.damage         = 15f;
        this.moveSpeed      = 50f;
        this.attackRange    = 150f; // arqueiros atacam de longe
        this.attackCooldown = 1.5f;
        this.attackTimer    = 0f;
        this.effectTimer    = 0f;
        this.speedMultiplier = 1.0f;
        this.isDead         = false;
        this.currentBehavior = UnitBehavior.SEEK_CLOSEST;
    }

    // Necessário para o Pool do libGDX reutilizar objetos sem new
    public void reset() {
        type = null;
        position.setZero();
        velocity.setZero();
        isDead = false;
        effectTimer = 0f;
        attackTimer = 0f;
        speedMultiplier = 1.0f;
        currentBehavior = UnitBehavior.SEEK_CLOSEST;
    }
}
