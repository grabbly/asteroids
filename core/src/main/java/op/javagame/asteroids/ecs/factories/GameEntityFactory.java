package op.javagame.asteroids.ecs.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
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
        entity.add(new HealthComponent(3));
        return entity;
    }
    public static Entity createBackground() {
        Entity entity = new Entity();
        entity.add(new BackgroundComponent("textures/backgrounds/blue.png"));
        return entity;
    }
    public static Entity createAsteroid(Vector2 position, boolean isSplit) {
        Entity entity = new Entity();
        entity.add(new PositionComponent(position));

        String texturePath;

        if (isSplit) {
            // Choose a random small asteroid sprite for split asteroids
            String[] smallAsteroids = {
                "textures/meteors/meteorBrown_med1.png",
                "textures/meteors/meteorBrown_med3.png",
                "textures/meteors/meteorGrey_med1.png",
                "textures/meteors/meteorGrey_med2.png"
            };
            texturePath = smallAsteroids[MathUtils.random(smallAsteroids.length - 1)];
        } else {
            // Use the original large asteroid
            String[] bigAsteroids = {
                "textures/meteors/meteorBrown_big1.png",
                "textures/meteors/meteorBrown_big2.png",
                "textures/meteors/meteorGrey_big2.png",
                "textures/meteors/meteorGrey_big3.png"
            };
            texturePath = bigAsteroids[MathUtils.random(bigAsteroids.length - 1)];
        }

        entity.add(new TextureComponent(texturePath));
        float colliderSize = isSplit ? 15 : 30; // Smaller collider for split asteroids
        entity.add(new ColliderComponent(colliderSize));
        entity.add(new DestroyOnCollisionComponent());

        return entity;
    }

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
        laser.add(new ColliderComponent(5));             // Коллайдер для лазера

        return laser;
    }

}
