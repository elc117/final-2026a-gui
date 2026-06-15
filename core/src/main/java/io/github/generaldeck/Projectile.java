package io.github.generaldeck;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Projectile implements Pool.Poolable {
    public Vector2 position = new Vector2();
    public Vector2 direction = new Vector2();
    public float angle;
    public float speed = 400f;
    public float damage;
    public int team;
    public boolean isDead = false;

    public void init(float startX, float startY, float targetX, float targetY, float damage, int team) {
        this.position.set(startX, startY);
        this.damage = damage;
        this.isDead = false;
        this.team = team;

        float dx = targetX - startX;
        float dy = targetY - startY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            this.direction.set(dx / distance, dy / distance);
        } else {
            this.direction.set(1, 0);
        }
        this.angle = MathUtils.atan2(this.direction.y, this.direction.x) * MathUtils.radiansToDegrees;
    }

    @Override
    public void reset() {
        this.isDead = false;
        this.direction.setZero();
        this.position.setZero();
    }
}
