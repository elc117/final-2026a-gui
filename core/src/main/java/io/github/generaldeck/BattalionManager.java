package io.github.generaldeck;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BattalionManager {

    private final Pool<Unit> unitPool = new Pool<Unit>() {
        @Override
        protected Unit newObject() {
            return new Unit();
        }
    };

    private final Array<Unit> activeUnits = new Array<>(512);

    public void spawnUnit(String type, float x, float y) {
        Unit unit = unitPool.obtain();

        if ("ARCHER".equals(type)) {
            unit.setToArcher(1, x, y);
        } else {
            unit.setToWarrior(0, x, y);
        }

        activeUnits.add(unit);
    }

    public void update(float delta) {
        for(int i = activeUnits.size - 1; i >= 0; i--) {
            Unit unit = activeUnits.get(i);

            if(unit.effectTimer > 0) {
                unit.effectTimer -= delta;
                if(unit.effectTimer <= 0) {
                    unit.currentBehavior = UnitBehavior.SEEK_CLOSEST;
                    unit.speedMultiplier = 1.0f;
                }
            }

            // MÁQUINA DE ESTADOS
            switch (unit.currentBehavior) {
                case SEEK_CLOSEST:
                    applySeekMath(unit, findClosestEnemy(unit));
                    break;
                case SEEK_LOWEST_HP:
                    applySeekMath(unit, findLowestHpEnemy(unit));
                    break;
                case FLEE:
                    applyFleeMath(unit, findClosestEnemy(unit));
                    break;
                case STUNNED:
                    unit.velocity.setZero();
                    break;
            }

            applySeparationMath(unit);

            unit.position.x += unit.velocity.x * unit.speedMultiplier * delta;
            unit.position.x = Math.clamp(unit.position.x, 0f, GameConfig.V_WIDTH);
            unit.position.y += unit.velocity.y * unit.speedMultiplier * delta;
            unit.position.y = Math.clamp(unit.position.y, 0f, GameConfig.V_HEIGHT);

            if(unit.isDead) {
                activeUnits.removeIndex(i);
                unitPool.free(unit);
            }
        }
    }

    private Unit findClosestEnemy(Unit seeker) {
        Unit closest = null;
        float minDistanceSq = Float.MAX_VALUE;

        for (int i = 0; i < activeUnits.size; i++) {
            Unit other = activeUnits.get(i);

            // Ignorar aliados e ignorar a si próprio
            if (other.team == seeker.team) continue;

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
            return;
        }

        float dirX = target.position.x - seeker.position.x;
        float dirY = target.position.y - seeker.position.y;
        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (distance > seeker.attackRange) {
            dirX /= distance;
            dirY /= distance;
            seeker.velocity.x = dirX * seeker.moveSpeed;
            seeker.velocity.y = dirY * seeker.moveSpeed;
        } else {
            seeker.velocity.setZero();
        }
    }

    private void applyFleeMath(Unit fleer, Unit threat) {
        if (threat == null) {
            fleer.velocity.setZero();
            return;
        }

        float dirX = fleer.position.x - threat.position.x;
        float dirY = fleer.position.y - threat.position.y;
        float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

        if (distance > 0) {
            dirX /= distance;
            dirY /= distance;
            fleer.velocity.x = dirX * fleer.moveSpeed;
            fleer.velocity.y = dirY * fleer.moveSpeed;
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
        // Se as suas bolinhas têm raio 8, 16 a 20 é um bom valor.
        float separationRadius = 20f;
        float separationForce = 5f; // O quão forte elas se empurram (ajuste a gosto)

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

    public void render(ShapeRenderer shapeRenderer) {
        for (int i = 0; i < activeUnits.size; i++) {
            Unit unit = activeUnits.get(i);

            // No futuro, teremos um atributo 'team' (0 = Aliado, 1 = Inimigo).
            // Por agora, pintamos de Verde os Arqueiros (longo alcance) e Vermelho os Guerreiros.
            if (unit.attackRange > 50f) {
                unit.currentBehavior = UnitBehavior.FLEE;
                shapeRenderer.setColor(Color.GREEN);
            } else {
                shapeRenderer.setColor(Color.RED);
            }

            // Desenha a unidade como um círculo sólido de raio 8 pixels
            // Acesso direto aos atributos públicos, sem métodos getPosition()
            shapeRenderer.circle(unit.position.x, unit.position.y, 8f);
        }
    }
}
