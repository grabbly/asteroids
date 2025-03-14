package op.javagame.asteroids.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import op.javagame.asteroids.ecs.components.*;
import op.javagame.asteroids.events.EventBus;
import op.javagame.asteroids.events.GameEvents;

public class CollisionSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<ColliderComponent> colliderMapper = ComponentMapper.getFor(ColliderComponent.class);
    private ComponentMapper<PlayerComponent> playerMapper = ComponentMapper.getFor(PlayerComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, ColliderComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        Entity player = null;
        Vector2 playerPos = new Vector2();
        float playerRadius = 0;

        for (Entity entity : entities) {
            if (playerMapper.has(entity)) {
                player = entity;
                PositionComponent pos = positionMapper.get(entity);
                ColliderComponent col = colliderMapper.get(entity);
                if (pos != null && col != null) {
                    playerPos.set(pos.position);
                    playerRadius = col.radius;
                }
                break;
            }
        }

        if (player == null) return;

        for (Entity entity : entities) {
            if (entity == player) continue;

            PositionComponent pos = positionMapper.get(entity);
            ColliderComponent col = colliderMapper.get(entity);

            if (pos != null && col != null) {
                float distance = playerPos.dst(pos.position);
                float combinedRadius = playerRadius + col.radius;

                if (distance < combinedRadius) {
                    EventBus.INSTANCE.notify(new GameEvents.RestartGameEvent());
                }
            }
        }
    }
}
