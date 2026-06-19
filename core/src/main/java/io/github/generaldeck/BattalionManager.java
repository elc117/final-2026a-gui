package io.github.generaldeck;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;

public class BattalionManager {

    private final Pool<Unit> unitPool = new Pool<Unit>() {
        @Override
        protected Unit newObject() {
            return new Unit();
        }
    };
    private final Array<Unit> activeUnits = new Array<>(1024);

    private final Pool<Projectile> projectilePool = new Pool<Projectile>() {
        @Override
        protected Projectile newObject() {
            return new Projectile();
        }
    };
    public Array<Projectile> activeProjectiles = new Array<>(1024);

    public void spawnUnit(String type, float x, float y, int team) {
        Unit unit = unitPool.obtain();

        switch(type) {
            case "ARCHER":
                unit.setToArcher(team, x, y);
                break;
            case "WARRIOR":
                unit.setToWarrior(team, x, y);
                break;
            case "MONK":
                unit.setToMonk(team, x, y);
                break;
            default:
                Gdx.app.error("Spawns", "Tipo de unidade desconhecido: " + type);
                unitPool.free(unit);
                return;
        }

        activeUnits.add(unit);
    }

    private void spawnProjectile(Unit shooter, Unit target) {
        Projectile proj = projectilePool.obtain();
        proj.init(shooter.position.x, shooter.position.y, target.position.x, target.position.y, shooter.damage, shooter.team);
        activeProjectiles.add(proj);
    }

    private void applyDamage(Unit target, float damage) {
        target.currentHp -= damage;
        if (target.currentHp <= 0) {
            target.currentHp = 0;
            target.isDead = true;
        }
    }

    public void update(float delta) {
        // UNIDADES
        for (int i = activeUnits.size - 1; i >= 0; i--) {
            Unit unit = activeUnits.get(i);

            if (unit.isDead) {
                activeUnits.removeIndex(i);
                unitPool.free(unit);
                continue;
            }

            unit.animTime += delta;
            if (unit.attackTimer > 0) unit.attackTimer -= delta;

            if (unit.effectTimer > 0) {
                unit.effectTimer -= delta;
                if (unit.effectTimer <= 0) {
                    unit.currentBehavior = UnitBehavior.SEEK_CLOSEST;
                    unit.speedMultiplier = 1.0f;
                }
            }

            // ATUALIZAÇÃO DO TEMPO DA ANIMAÇÃO DE CURA NO ALVO
            if (unit.isReceivingHeal) {
                unit.healEffectAnimTime += delta;
                // Se a animação das estrelinhas acabou, desliga a flag
                if (unit.healEffectAnimTime >= AnimationManager.monkHealEffect.getAnimationDuration()) {
                    unit.isReceivingHeal = false;
                    unit.healEffectAnimTime = 0f;
                }
            }

            if (unit.isAttacking) {
                unit.attackLockTimer -= delta;
                unit.velocity.setZero();

                if (unit.attackLockTimer > 0) {
                    continue;
                }
                unit.isAttacking = false;
            }

            if (unit.isHealer) {
                processHealerLogic(unit);
            } else {
                switch (unit.currentBehavior) {
                    case SEEK_CLOSEST:
                        processCombatAndMovement(unit, findClosestEnemy(unit), false);
                        break;
                    case SEEK_LOWEST_HP:
                        processCombatAndMovement(unit, findLowestHpEnemy(unit), false);
                        break;
                    case FLEE:
                        processCombatAndMovement(unit, findClosestEnemy(unit), true);
                        break;
                    default:
                        unit.velocity.setZero();
                        break;
                }
            }

            applySeparationMath(unit);

            unit.position.x += unit.velocity.x * unit.speedMultiplier * delta;
            unit.position.x = MathUtils.clamp(unit.position.x, GameConfig.UNIT_DRAW_SIZE,
                GameConfig.V_WIDTH - GameConfig.UNIT_DRAW_SIZE);

            unit.position.y += unit.velocity.y * unit.speedMultiplier * delta;
            unit.position.y = MathUtils.clamp(unit.position.y, GameConfig.UNIT_DRAW_SIZE + 100f,
                GameConfig.V_HEIGHT - GameConfig.UNIT_DRAW_SIZE - 100f);
        }

        // PROJETEIS
        for (int i = activeProjectiles.size - 1; i >= 0; i--) {
            Projectile proj = activeProjectiles.get(i);

            proj.position.x += proj.direction.x * proj.speed * delta;
            proj.position.y += proj.direction.y * proj.speed * delta;

            if (proj.position.x < 0 || proj.position.x > GameConfig.V_WIDTH ||
                proj.position.y < 0 || proj.position.y > GameConfig.V_HEIGHT) {
                activeProjectiles.removeIndex(i);
                projectilePool.free(proj);
                continue;
            }

            boolean hitSomebody = false;
            float radius = GameConfig.SPRITE_DRAW_SIZE / 3f;
            float hitRadiusSq = radius * radius;

            for (int j = 0; j < activeUnits.size; j++) {
                Unit potentialTarget = activeUnits.get(j);

                if (potentialTarget.isDead || potentialTarget.team == proj.team) continue;

                float dx = potentialTarget.position.x - proj.position.x;
                float dy = potentialTarget.position.y - proj.position.y;
                float distSq = (dx * dx) + (dy * dy);

                if (distSq <= hitRadiusSq) {
                    applyDamage(potentialTarget, proj.damage);
                    hitSomebody = true;
                    break;
                }
            }

            if (hitSomebody) {
                activeProjectiles.removeIndex(i);
                projectilePool.free(proj);
            }
        }
    }

    private Unit findLowestHpAlly(Unit healer) {
        Unit bestTarget = null;
        float lowestHpRatio = 1.0f;
        float minDistanceSq = Float.MAX_VALUE;

        for (int i = 0; i < activeUnits.size; i++) {
            Unit other = activeUnits.get(i);

            if (other.isDead || other.team != healer.team || other == healer) continue;

            float hpRatio = other.currentHp / other.maxHp;
            float dx = other.position.x - healer.position.x;
            float dy = other.position.y - healer.position.y;
            float distSq = dx * dx + dy * dy;

            if (hpRatio < 1.0f) {
                if (hpRatio < lowestHpRatio) {
                    lowestHpRatio = hpRatio;
                    minDistanceSq = distSq;
                    bestTarget = other;
                } else if (hpRatio == lowestHpRatio && distSq < minDistanceSq) {
                    minDistanceSq = distSq;
                    bestTarget = other;
                }
            } else if (lowestHpRatio == 1.0f) {
                if (distSq < minDistanceSq) {
                    minDistanceSq = distSq;
                    bestTarget = other;
                }
            }
        }
        return bestTarget;
    }

    private void processHealerLogic(Unit healer) {
        Unit target = findLowestHpAlly(healer);
        if (target == null) {
            healer.velocity.setZero();
            return;
        }

        float dx = target.position.x - healer.position.x;
        float dy = target.position.y - healer.position.y;
        float distSq = (dx * dx) + (dy * dy);
        float rangeSq = healer.attackRange * healer.attackRange;

        if (target.currentHp < target.maxHp && distSq <= rangeSq) {
            healer.velocity.setZero();
            healer.facingRight = dx > 0;

            if (healer.attackTimer <= 0) {
                healer.isAttacking = true;
                healer.animTime = 0f;
                healer.attackLockTimer = healer.attackDuration;
                healer.attackTimer = healer.attackCooldown;

                target.currentHp += healer.healAmount;
                if (target.currentHp > target.maxHp) target.currentHp = target.maxHp;

                // AVISA O ALVO QUE ELE FOI CURADO PARA EXIBIR A ANIMAÇÃO
                target.isReceivingHeal = true;
                target.healEffectAnimTime = 0f;
            }
        } else if (target.currentHp == target.maxHp && distSq <= rangeSq * 0.25f) {
            healer.velocity.setZero();
            healer.facingRight = dx > 0;
        } else {
            applySeekMath(healer, target);
        }
    }

    private Unit findClosestEnemy(Unit seeker) {
        Unit closest = null;
        float minDistanceSq = Float.MAX_VALUE;

        for (int i = 0; i < activeUnits.size; i++) {
            Unit other = activeUnits.get(i);

            if (other.isDead || other.team == seeker.team) continue;

            float dx = other.position.x - seeker.position.x;
            float dy = other.position.y - seeker.position.y;
            float distSq = dx * dx + dy * dy;

            if (distSq < minDistanceSq) {
                minDistanceSq = distSq;
                closest = other;
            }
        }
        return closest;
    }

    private void applySeekMath(Unit seeker, Unit target) {
        if (target == null) {
            seeker.velocity.setZero();
            return;
        }

        float dirX     = target.position.x - seeker.position.x;
        float dirY     = target.position.y - seeker.position.y;
        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (distance > 0) {
            dirX /= distance;
            dirY /= distance;
        }

        seeker.velocity.x = dirX * seeker.moveSpeed;
        seeker.velocity.y = dirY * seeker.moveSpeed;

        if (Math.abs(dirX) > 0.01f) seeker.facingRight = dirX > 0;
    }

    private void applyFleeMath(Unit fleer, Unit threat) {
        if (threat == null) {
            fleer.velocity.setZero();
            fleer.isAttacking = false;
            return;
        }

        float dirX     = fleer.position.x - threat.position.x;
        float dirY     = fleer.position.y - threat.position.y;
        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (distance > 0) {
            dirX /= distance;
            dirY /= distance;
            fleer.velocity.x  = dirX * fleer.moveSpeed;
            fleer.velocity.y  = dirY * fleer.moveSpeed;
            fleer.isAttacking = false;
            if (Math.abs(dirX) > 0.01f) fleer.facingRight = dirX > 0;
        }
    }

    private Unit findLowestHpEnemy(Unit seeker) {
        Unit lowestHpUnit = null;
        float minHp = Float.MAX_VALUE;

        for (int i = 0; i < activeUnits.size; i++) {
            Unit other = activeUnits.get(i);
            if (other.team == seeker.team) continue;

            if (other.currentHp < minHp) {
                minHp = other.currentHp;
                lowestHpUnit = other;
            }
        }
        return lowestHpUnit != null ? lowestHpUnit : findClosestEnemy(seeker);
    }

    private void applySeparationMath(Unit unit) {
        float pushX = 0;
        float pushY = 0;
        float separationRadius = 20f;
        float separationForce = 10f;

        for (int i = 0; i < activeUnits.size; i++) {
            Unit other = activeUnits.get(i);

            if (unit == other) continue;

            float dx = unit.position.x - other.position.x;
            float dy = unit.position.y - other.position.y;
            float distSq = dx * dx + dy * dy;

            if (distSq > 0 && distSq < (separationRadius * separationRadius)) {
                float distance = (float) Math.sqrt(distSq);
                float overlap = separationRadius - distance;
                dx /= distance;
                dy /= distance;
                pushX += dx * overlap;
                pushY += dy * overlap;
            }
        }

        unit.velocity.x += pushX * separationForce;
        unit.velocity.y += pushY * separationForce;
    }

    private void processCombatAndMovement(Unit unit, Unit target, boolean isFleeing) {
        if (target == null) {
            unit.velocity.setZero();
            return;
        }

        float dx = target.position.x - unit.position.x;
        float dy = target.position.y - unit.position.y;
        float distSq = (dx * dx) + (dy * dy);
        float rangeSq = unit.attackRange * unit.attackRange;

        if (distSq <= rangeSq && !isFleeing) {
            unit.velocity.setZero();
            unit.facingRight = dx > 0;

            if (unit.attackTimer <= 0) {
                unit.isAttacking = true;
                unit.animTime = 0f;
                unit.attackLockTimer = unit.attackDuration;
                unit.attackTimer = unit.attackCooldown;

                unit.useAlternateAttack = MathUtils.randomBoolean();

                if (unit.isRanged) {
                    spawnProjectile(unit, target);
                } else {
                    applyDamage(target, unit.damage);
                }
            }
        } else {
            if (isFleeing) {
                applyFleeMath(unit, target);
            } else {
                applySeekMath(unit, target);
            }
        }
    }

    public void spawnArmies(String[][] playerGrid, String[][] enemyGrid) {
        int cols = playerGrid.length;
        int rows = playerGrid[0].length;

        float gridPixelWidth = cols * GameConfig.TILE_SIZE;
        float gridPixelHeight = rows * GameConfig.TILE_SIZE;

        float startY = (GameConfig.V_HEIGHT - gridPixelHeight) / 2f;
        float screenMargin = GameConfig.V_WIDTH * 0.1f;

        float playerStartX = screenMargin;
        parseGridAndSpawnUnits(playerGrid, playerStartX, startY, 0, false);

        float enemyStartX = GameConfig.V_WIDTH - screenMargin - gridPixelWidth;
        parseGridAndSpawnUnits(enemyGrid, enemyStartX, startY, 1, true);
    }

    private void parseGridAndSpawnUnits(String[][] gridState, float startX, float startY, int team, boolean isMirrored) {
        int cols = gridState.length;
        int rows = gridState[0].length;

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                String unitType = gridState[x][y];

                if (unitType != null) {
                    int effectiveX = isMirrored ? (cols - 1 - x) : x;
                    float worldX = startX + (effectiveX * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2f);
                    float worldY = startY + (y * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2f);
                    spawnSquad(unitType, worldX, worldY, 30, team);
                }
            }
        }
    }

    private void spawnSquad(String type, float centerX, float centerY, int count, int team) {
        for (int i = 0; i < count; i++) {
            float jitterX = (float) (Math.random() * 100);
            float jitterY = (float) (Math.random() * 100);
            spawnUnit(type, centerX + jitterX, centerY + jitterY, team);
        }
    }

    public void render(SpriteBatch batch, float stateTime) {
        // --- CAMADA 1: DESENHA TODAS AS TROPAS PRIMEIRO ---
        for (int i = 0; i < activeUnits.size; i++) {
            Unit unit = activeUnits.get(i);
            Animation<TextureRegion> anim;

            // Descobre se a unidade está andando (velocidade maior que um limite mínimo)
            boolean isMoving = unit.velocity.len2() >= 25f;

            switch(unit.type) {
                case "ARCHER":
                    if (unit.isAttacking) anim = AnimationManager.archerShoot;
                    else if (isMoving) anim = AnimationManager.archerRun;
                    else anim = AnimationManager.archerIdle;
                    break;
                case "WARRIOR":
                    if (unit.isAttacking) anim = unit.useAlternateAttack ? AnimationManager.warriorAttack_alt : AnimationManager.warriorAttack;
                    else if (isMoving) anim = AnimationManager.warriorRun;
                    else anim = AnimationManager.warriorIdle;
                    break;
                case "MONK":
                    if (unit.isAttacking) anim = AnimationManager.monkHeal;
                    else if (isMoving) anim = AnimationManager.monkRun;
                    else anim = AnimationManager.monkIdle;
                    break;
                default:
                    anim = AnimationManager.warriorIdle;
            }

            TextureRegion frame = anim.getKeyFrame(unit.animTime);

            float drawH = GameConfig.SPRITE_DRAW_SIZE;
            float ratio = (float) frame.getRegionWidth() / frame.getRegionHeight();
            float drawW = drawH * ratio;

            float drawX = unit.position.x - (drawW / 2f);
            float drawY = unit.position.y - (drawH / 2f);

            if (unit.facingRight) {
                batch.draw(frame, drawX, drawY, drawW, drawH);
            } else {
                batch.draw(frame, drawX + drawW, drawY, -drawW, drawH);
            }
        }

        // --- CAMADA 2: EFEITOS VISUAIS PARA DESTACAR O EFEITO DE CURA
        for (int i = 0; i < activeUnits.size; i++) {
            Unit unit = activeUnits.get(i);

            if (unit.isReceivingHeal) {
                TextureRegion effectFrame = AnimationManager.monkHealEffect.getKeyFrame(unit.healEffectAnimTime);

                float effectRatio = (float) effectFrame.getRegionWidth() / effectFrame.getRegionHeight();

                float effectH = GameConfig.SPRITE_DRAW_SIZE * 1.2f;
                float effectW = effectH * effectRatio;

                float effectX = unit.position.x - (effectW / 2f);
                float effectY = unit.position.y - (effectH / 2f) + 10f;

                batch.draw(effectFrame, effectX, effectY, effectW, effectH);
            }
        }

        // --- CAMADA 3: DESENHA PROJÉTEIS ---
        if (AnimationManager.arrowIcon != null) {
            TextureRegion arrowTex = AnimationManager.arrowIcon;
            float w = arrowTex.getRegionWidth() * 0.3f;
            float h = arrowTex.getRegionHeight() * 0.3f;

            for (int i = 0; i < activeProjectiles.size; i++) {
                Projectile proj = activeProjectiles.get(i);
                batch.draw(
                    arrowTex,
                    proj.position.x - w/2f, proj.position.y - h/2f,
                    w/2f, h/2f, w, h, 1f, 1f, proj.angle
                );
            }
        }
    }

    // Retorna 'true' se todas as unidades de um time específico estiverem mortas
    public boolean isTeamDead(int teamId) {
        for (int i = 0; i < activeUnits.size; i++) {
            Unit u = activeUnits.get(i);
            if (u.team == teamId && !u.isDead) {
                return false; // Achou alguém vivo, o time ainda não perdeu
            }
        }
        return true; // Ninguém vivo encontrado
    }

    public void dispose() {
        activeUnits.clear();
        activeProjectiles.clear();
        unitPool.clear();
        projectilePool.clear();
    }
}
