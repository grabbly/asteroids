package op.javagame.asteroids.ecs.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import op.javagame.asteroids.ecs.components.*;

public class GameEntityFactory {

    public static Entity createPlayer(Vector2 position) {
        Entity entity = new Entity();
        entity.add(new PositionComponent(position));
        entity.add(new TextureComponent("textures/ships/playerShip1_blue.png"));
        entity.add(new VelocityComponent(new Vector2()));
        entity.add(new RotationComponent());
        entity.add(new PlayerComponent());
        entity.add(new ColliderComponent(25));
        return entity;
    }
    public static Entity createBackground() {
        Entity entity = new Entity();
        entity.add(new BackgroundComponent("textures/backgrounds/blue.png"));
        return entity;
    }
    public static Entity createAsteroid(Vector2 position) {
        Entity entity = new Entity();
        entity.add(new PositionComponent(position));
        entity.add(new TextureComponent("textures/meteors/meteorBrown_big1.png"));
        entity.add(new ColliderComponent(30));
        entity.add(new DestroyOnCollisionComponent());
        return entity;
    }

    /**
     * Создаёт врага.
     */
    public static Entity createEnemy(Vector2 position) {
        Entity entity = new Entity();
        entity.add(new PositionComponent(position));
        entity.add(new TextureComponent("textures/enemies/enemyRed1.png"));
        return entity;
    }

    public static Entity createLaser(Vector2 position, float angle) {
        Entity laser = new Entity();

        Vector2 velocity = new Vector2(0, 800).rotateDeg(angle);

        laser.add(new PositionComponent(position.cpy())); // Позиция
        laser.add(new VelocityComponent(velocity));       // Скорость
        laser.add(new TextureComponent("textures/lasers/laserBlue01.png")); // Текстура
        laser.add(new RotationComponent(angle));          // Угол поворота
        laser.add(new LaserComponent());                 // Помечаем как лазер

        return laser;
    }

}
