package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Unit implements Pool.Poolable {
    public String  type;
    public Vector2 position        = new Vector2();
    public Vector2 velocity        = new Vector2();
    public int     team;
    public float   currentHp, maxHp;
    public float   damage;
    public float   moveSpeed;
    public float   attackRange;
    public float   attackCooldown;
    public float   attackTimer;
    public float   effectTimer;
    public float   speedMultiplier = 1.0f;
    public boolean isDead;
    public UnitBehavior currentBehavior = UnitBehavior.SEEK_CLOSEST;

    public boolean facingRight = true;  // espelha o sprite
    public boolean isAttacking = false; // escolhe walk vs attack animation
    public float   animTime    = 0f;
    public float attackDuration;
    public float attackLockTimer = 0f;
    public boolean isRanged;

    public void setToWarrior(int team, float x, float y) {
        this.type            = "WARRIOR";
        this.team            = team;
        this.position.set(x, y);
        this.velocity.setZero();
        this.maxHp           = 140f;
        this.currentHp       = maxHp;
        this.damage          = 10f;
        this.moveSpeed       = 60f;
        this.attackRange     = 30f;
        this.attackCooldown  = 1.0f;
        this.attackTimer     = 0f;
        this.effectTimer     = 0f;
        this.speedMultiplier = 1.0f;
        this.isDead          = false;
        this.currentBehavior = UnitBehavior.SEEK_CLOSEST;
        this.facingRight     = (team == 0); // jogador vai pra direita, inimigo pra esquerda
        this.isAttacking     = false;
        this.animTime        = 0f;
        this.attackDuration  = 0.8f;
        this.isRanged = false;
    }

    public void setToArcher(int team, float x, float y) {
        this.type            = "ARCHER";
        this.team            = team;
        this.position.set(x, y);
        this.velocity.setZero();
        this.maxHp           = 70f;
        this.currentHp       = maxHp;
        this.damage          = 15f;
        this.moveSpeed       = 50f;
        this.attackRange     = 300f;
        this.attackCooldown  = 1.5f;
        this.attackTimer     = 0f;
        this.effectTimer     = 0f;
        this.speedMultiplier = 1.0f;
        this.isDead          = false;
        this.currentBehavior = UnitBehavior.SEEK_CLOSEST;
        this.facingRight     = (team == 0);
        this.isAttacking     = false;
        this.animTime        = 0f;
        this.attackDuration  = 3.2f;
        this.isRanged = true;
    }

    @Override
    public void reset() {
        type             = null;
        position.setZero();
        velocity.setZero();
        isDead           = false;
        effectTimer      = 0f;
        attackTimer      = 0f;
        speedMultiplier  = 1.0f;
        currentBehavior  = UnitBehavior.SEEK_CLOSEST;
        isAttacking      = false;
        animTime         = 0f;
        attackLockTimer  = 0f;
    }

    public static int getCost(String type) {
        switch(type) {
            case "WARRIOR": return 100;
            case "ARCHER":  return 150;
            default:        return 0; // Fallback de segurança
        }
    }
}
