package op.javagame.asteroids.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import op.javagame.asteroids.ecs.components.PositionComponent;
import op.javagame.asteroids.ecs.components.VelocityComponent;
import op.javagame.asteroids.ecs.factories.GameEntityFactory;

public class AsteroidSystem extends EntitySystem {
    private static final float SPAWN_INTERVAL = 2f;  // Интервал спавна
    private static final float ASTEROID_SPEED = 100f;  // Скорость астероидов
    private static final float SPAWN_MARGIN = 50f;  // Отступ за границами экрана
    private static final int MAX_ASTEROIDS = 10; // Максимальное количество астероидов

    private float timeSinceLastSpawn = 0;
    private Engine engine;

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void update(float deltaTime) {
        timeSinceLastSpawn += deltaTime;

        if (timeSinceLastSpawn >= SPAWN_INTERVAL && getAsteroidCount() < MAX_ASTEROIDS) {
            spawnAsteroid();
            timeSinceLastSpawn = 0;
        }
    }

    private void spawnAsteroid() {
        if (engine == null) return;

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        int side = MathUtils.random(3);  // 0 - верх, 1 - низ, 2 - слева, 3 - справа
        float x = 0, y = 0;

        switch (side) {
            case 0: // Верхняя граница
                x = MathUtils.random(-SPAWN_MARGIN, screenWidth + SPAWN_MARGIN);
                y = screenHeight + SPAWN_MARGIN;
                break;
            case 1: // Нижняя граница
                x = MathUtils.random(-SPAWN_MARGIN, screenWidth + SPAWN_MARGIN);
                y = -SPAWN_MARGIN;
                break;
            case 2: // Левая граница
                x = -SPAWN_MARGIN;
                y = MathUtils.random(-SPAWN_MARGIN, screenHeight + SPAWN_MARGIN);
                break;
            case 3: // Правая граница
                x = screenWidth + SPAWN_MARGIN;
                y = MathUtils.random(-SPAWN_MARGIN, screenHeight + SPAWN_MARGIN);
                break;
        }

        Vector2 direction = new Vector2(screenWidth / 2f - x, screenHeight / 2f - y).nor();
        Vector2 velocity = direction.scl(ASTEROID_SPEED);

        Entity asteroid = GameEntityFactory.createAsteroid(new Vector2(x, y));
        asteroid.add(new VelocityComponent(velocity)); // Добавляем скорость

        engine.addEntity(asteroid);
        Gdx.app.log("AsteroidSystem", "🪨 Spawned asteroid at: " + x + ", " + y + " moving to " + velocity);
    }

    private int getAsteroidCount() {
        return engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get()).size();
    }
}
