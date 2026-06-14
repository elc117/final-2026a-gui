package io.github.generaldeck;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    public void spawnUnit(String type, float x, float y, int team) {
        Unit unit = unitPool.obtain();

        if ("ARCHER".equals(type)) {
            unit.setToArcher(team, x, y);
        } else {
            unit.setToWarrior(team, x, y);
        }

        activeUnits.add(unit);
    }

    public void update(float delta) {
        for (int i = activeUnits.size - 1; i >= 0; i--) {
            Unit unit = activeUnits.get(i);

            if (unit.isDead) {
                activeUnits.removeIndex(i);
                unitPool.free(unit);
                continue;
            }

            unit.animTime += delta; // <-- novo: timer por unidade

            if (unit.attackTimer > 0) unit.attackTimer -= delta;

            if (unit.effectTimer > 0) {
                unit.effectTimer -= delta;
                if (unit.effectTimer <= 0) {
                    unit.currentBehavior = UnitBehavior.SEEK_CLOSEST;
                    unit.speedMultiplier = 1.0f;
                }
            }

            // máquina de estados
            switch (unit.currentBehavior) {
                case SEEK_CLOSEST:  applySeekMath(unit, findClosestEnemy(unit));  break;
                case SEEK_LOWEST_HP:applySeekMath(unit, findLowestHpEnemy(unit)); break;
                case FLEE:          applyFleeMath(unit, findClosestEnemy(unit));  break;
                case STUNNED:
                    unit.velocity.setZero();
                    unit.isAttacking = false;
                    break;
            }

            applySeparationMath(unit);

            unit.position.x += unit.velocity.x * unit.speedMultiplier * delta;
            unit.position.x = MathUtils.clamp(unit.position.x, GameConfig.UNIT_DRAW_SIZE,
                GameConfig.V_WIDTH - GameConfig.UNIT_DRAW_SIZE);

            unit.position.y += unit.velocity.y * unit.speedMultiplier * delta;
            unit.position.y = MathUtils.clamp(unit.position.y, GameConfig.UNIT_DRAW_SIZE,
                GameConfig.V_HEIGHT - GameConfig.UNIT_DRAW_SIZE);
        }
    }

    private Unit findClosestEnemy(Unit seeker) {
        Unit closest = null;
        float minDistanceSq = Float.MAX_VALUE;

        for (int i = 0; i < activeUnits.size; i++) {
            Unit other = activeUnits.get(i);

            // Ignorar aliados e ignorar a si próprio
            if (other.isDead || other.team == seeker.team) continue;

            // Distância em X e Y
            float dx = other.position.x - seeker.position.x;
            float dy = other.position.y - seeker.position.y;

            // Distância ao quadrado (Mais rápido que fazer Math.sqrt)
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
            seeker.isAttacking = false;
            return;
        }

        float dirX     = target.position.x - seeker.position.x;
        float dirY     = target.position.y - seeker.position.y;
        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (distance > seeker.attackRange) {
            dirX /= distance;
            dirY /= distance;
            seeker.velocity.x    = dirX * seeker.moveSpeed;
            seeker.velocity.y    = dirY * seeker.moveSpeed;
            seeker.isAttacking   = false;

            if (Math.abs(dirX) > 0.01f) seeker.facingRight = dirX > 0;
        } else {
            seeker.velocity.setZero();
            seeker.isAttacking = true;
            // olha para o alvo mesmo parado
            seeker.facingRight = target.position.x > seeker.position.x;

            if (seeker.attackTimer <= 0) {
                target.currentHp -= seeker.damage;
                seeker.attackTimer = seeker.attackCooldown;

                if (target.currentHp <= 0) {
                    target.currentHp = 0;
                    target.isDead    = true;
                }
            }
        }
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

            if (other.team == seeker.team) continue; // Ignora aliados

            if (other.currentHp < minHp) {
                minHp = other.currentHp;
                lowestHpUnit = other;
            }
        }
        return lowestHpUnit != null ? lowestHpUnit : findClosestEnemy(seeker); // Fallback caso haja empate/erro
    }

    private void applySeparationMath(Unit unit) {
        float pushX = 0;
        float pushY = 0;

        // Espaço pessoal da unidade (ex: 20 pixels de raio).
        float separationRadius = 20f;
        float separationForce = 10f; // O quão forte elas se empurram

        for (int i = 0; i < activeUnits.size; i++) {
            Unit other = activeUnits.get(i);

            // Não faz sentido separarmo-nos de nós próprios!
            if (unit == other) continue;

            float dx = unit.position.x - other.position.x;
            float dy = unit.position.y - other.position.y;

            float distSq = dx * dx + dy * dy;

            // Se o 'other' estiver dentro do nosso espaço pessoal...
            if (distSq > 0 && distSq < (separationRadius * separationRadius)) {
                float distance = (float) Math.sqrt(distSq);

                // Quanto mais perto, maior a sobreposição e mais forte é o empurrão!
                float overlap = separationRadius - distance;

                // Normaliza o vetor de repulsão
                dx /= distance;
                dy /= distance;

                // Acumula a força de empurrão
                pushX += dx * overlap;
                pushY += dy * overlap;
            }
        }

        // Adiciona a força acumulada de repulsão à velocidade atual da unidade
        unit.velocity.x += pushX * separationForce;
        unit.velocity.y += pushY * separationForce;
    }

    public void spawnArmies(String[][] playerGrid, String[][] enemyGrid) {
        int cols = playerGrid.length;
        int rows = playerGrid[0].length;

        float gridPixelWidth = cols * GameConfig.TILE_SIZE;
        float gridPixelHeight = rows * GameConfig.TILE_SIZE;

        float startY = (GameConfig.V_HEIGHT - gridPixelHeight) / 2f;

        float screenMargin = GameConfig.V_WIDTH * 0.1f;

        // spawna o JOGADOR (lado esquerdo, não espelhado, time 0)
        float playerStartX = screenMargin;
        parseGridAndSpawnUnits(playerGrid, playerStartX, startY, 0, false);

        // spawna o INIMIGO (lado direito, espelhado, time 1)
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
                    // lógica de espelhamento
                    int effectiveX = isMirrored ? (cols - 1 - x) : x;

                    // Converte a Coordenada da Matriz [X, Y] para Pixels no Mundo
                    float worldX = startX + (effectiveX * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2f);
                    float worldY = startY + (y * GameConfig.TILE_SIZE) + (GameConfig.TILE_SIZE / 2f);

                    // SPAWNA AS UNIDADES
                    spawnSquad(unitType, worldX, worldY, 50, team);
                }
            }
        }
    }

    private void spawnSquad(String type, float centerX, float centerY, int count, int team) {
        for (int i = 0; i < count; i++) {
            // Adiciona uma pequena variação (jitter) para as unidades não nascerem exatamente em cima da outra
            float jitterX = (float) (Math.random() * 100 - 20);
            float jitterY = (float) (Math.random() * 100 - 20);

            // O Pool entra em ação: Zero alocação de 'new Unit()' no Heap!
            spawnUnit(type, centerX + jitterX, centerY + jitterY, team);
        }
    }

    public void render(SpriteBatch batch, float stateTime) {
        for (int i = 0; i < activeUnits.size; i++) {
            Unit unit = activeUnits.get(i);

            // Escolhe a animação correta
            Animation<TextureRegion> anim;
            if ("ARCHER".equals(unit.type)) {
                anim = unit.isAttacking ? AnimationManager.archerShoot : AnimationManager.archerWalk;
            } else {
                anim = unit.isAttacking ? AnimationManager.warriorAttack : AnimationManager.warriorWalk;
            }

            // Usa o timer individual da unidade
            TextureRegion frame = anim.getKeyFrame(unit.animTime, true);

            float drawSize = GameConfig.SPRITE_DRAW_SIZE;
            float drawX    = unit.position.x - (drawSize / 2f);
            float drawY    = unit.position.y - (drawSize / 2f);

            if (unit.facingRight) {
                batch.draw(frame, drawX, drawY, drawSize, drawSize);
            } else {
                // Espelha horizontalmente invertendo o width
                batch.draw(frame, drawX + drawSize, drawY, -drawSize, drawSize);
            }
        }
    }
}
