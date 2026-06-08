package io.github.gameprototipo;

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
            unit.setToArcher(x, y);
        } else {
            unit.setToWarrior(x, y);
        }

        activeUnits.add(unit);
    }

    public void update(float delta) {
        for(int i = activeUnits.size - 1; i >= 0; i--) {
            Unit unit = activeUnits.get(i);

            // Lógica de IA
            // updatePosition

            if(unit.isDead) {
                activeUnits.removeIndex(i);
                unitPool.free(unit);
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (int i = 0; i < activeUnits.size; i++) {
            Unit unit = activeUnits.get(i);

            // No futuro, teremos um atributo 'team' (0 = Aliado, 1 = Inimigo).
            // Por agora, pintamos de Verde os Arqueiros (longo alcance) e Vermelho os Guerreiros.
            if (unit.attackRange > 50f) {
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
