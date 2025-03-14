package op.javagame.asteroids.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import op.javagame.asteroids.ecs.components.*;
import op.javagame.asteroids.events.EventBus;
import op.javagame.asteroids.events.GameEvents;
import op.javagame.asteroids.ecs.factories.GameEntityFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles collision detection between entities such as the player, asteroids, and lasers.
 */
public class CollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<ColliderComponent> colliderMapper = ComponentMapper.getFor(ColliderComponent.class);
    private final ComponentMapper<PlayerComponent> playerMapper = ComponentMapper.getFor(PlayerComponent.class);
    private final ComponentMapper<LaserComponent> laserMapper = ComponentMapper.getFor(LaserComponent.class);
    private final ComponentMapper<HealthComponent> healthMapper = ComponentMapper.getFor(HealthComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        // We gather all entities that have PositionComponent + ColliderComponent
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, ColliderComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        // We collect entities that need to be removed after iteration
        List<Entity> toRemove = new ArrayList<>();

        // Find the player entity (if any)
        Entity player = null;
        Vector2 playerPos = new Vector2();
        float playerRadius = 0;
        HealthComponent playerHealth = null;

        for (Entity entity : entities) {
            if (playerMapper.has(entity)) {
                player = entity;
                PositionComponent pos = positionMapper.get(entity);
                ColliderComponent col = colliderMapper.get(entity);
                playerHealth = healthMapper.get(entity); // Get player's health

                if (pos != null && col != null) {
                    playerPos.set(pos.position);
                    playerRadius = col.radius;
                }
                break;
            }
        }

        if (player == null) {
            // If there's no player, skip collision checks
            return;
        }

        // Convert ImmutableArray to a List of Entities for safe iteration
        List<Entity> entityList = new ArrayList<>(List.of(entities.toArray(Entity.class)));

        // Check collisions
        for (Entity asteroid : entityList) {
            // Skip if it's the player or a laser
            if (playerMapper.has(asteroid) || laserMapper.has(asteroid)) continue;

            PositionComponent asteroidPos = positionMapper.get(asteroid);
            ColliderComponent asteroidCol = colliderMapper.get(asteroid);

            if (asteroidPos == null || asteroidCol == null) continue;

            // Check collision with the player
            float distanceToPlayer = playerPos.dst(asteroidPos.position);
            float combinedRadius = playerRadius + asteroidCol.radius;
            if (distanceToPlayer < combinedRadius) {
                Gdx.app.log("COLLISION", "Player collided with asteroid");

                // If player has HealthComponent, reduce lives first
                if (playerHealth != null && playerHealth.lives > 0) {
                    playerHealth.lives--;
                    EventBus.INSTANCE.notify(new GameEvents.PlayerHitEvent(playerHealth.lives));
                    Gdx.app.log("COLLISION", "Player has " + playerHealth.lives + " lives left.");
                    player.add(new InvincibilityComponent());
                    // Remove the asteroid that hit the player
                    toRemove.add(asteroid);

                    // If lives reach 0, restart the game
                    if (playerHealth.lives <= 0) {
                        Gdx.app.log("COLLISION", "No more lives. Restarting game.");
                        EventBus.INSTANCE.notify(new GameEvents.RestartGameEvent());
                    }
                } else {
                    // If no HealthComponent or no lives left, restart game
                    Gdx.app.log("COLLISION", "Game Over! Restarting immediately.");
                    EventBus.INSTANCE.notify(new GameEvents.RestartGameEvent());
                }
                break;
            }

            // Check collision with lasers
            for (Entity laser : new ArrayList<>(List.of(entities.toArray(Entity.class)))) {
                if (!laserMapper.has(laser)) continue;

                PositionComponent laserPos = positionMapper.get(laser);
                ColliderComponent laserCol = colliderMapper.get(laser);

                if (laserPos == null || laserCol == null) continue;

                float distanceToLaser = laserPos.position.dst(asteroidPos.position);
                float combinedRadiusLaser = laserCol.radius + asteroidCol.radius;

                if (distanceToLaser < combinedRadiusLaser) {
                    Gdx.app.log("COLLISION", "Laser hit asteroid");

                    toRemove.add(laser);
                    toRemove.add(asteroid);

                    // Increase score by sending event
                    EventBus.INSTANCE.notify(new GameEvents.AsteroidDestroyedEvent());

                    // If asteroid is large, split into two
                    if (asteroidCol.radius > 15) {
                        splitAsteroid(asteroidPos.position, playerPos);
                    }
                }
            }
        }

        // Remove entities after iteration
        for (Entity entity : toRemove) {
            getEngine().removeEntity(entity);
        }
    }

    /**
     * Splits a large asteroid into two smaller ones upon destruction.
     */
    private void splitAsteroid(Vector2 asteroidPosition, Vector2 playerPosition) {
        float splitSpeed = 25;

        // Direction away from player
        Vector2 directionFromPlayer = asteroidPosition.cpy().sub(playerPosition).nor();

        Vector2 velocity1 = directionFromPlayer.cpy().rotateDeg(MathUtils.random(-20, -10)).scl(splitSpeed);
        Vector2 velocity2 = directionFromPlayer.cpy().rotateDeg(MathUtils.random(10, 20)).scl(splitSpeed);

        // Create smaller asteroids with "true" param for isSplit
        Entity smallAsteroid1 = GameEntityFactory.createAsteroid(asteroidPosition.cpy().add(10, 10), true);
        smallAsteroid1.add(new VelocityComponent(velocity1));
        getEngine().addEntity(smallAsteroid1);

        Entity smallAsteroid2 = GameEntityFactory.createAsteroid(asteroidPosition.cpy().add(-10, -10), true);
        smallAsteroid2.add(new VelocityComponent(velocity2));
        getEngine().addEntity(smallAsteroid2);

        Gdx.app.log("Asteroid", "Split into two smaller ones");
    }
}
