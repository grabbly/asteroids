package op.javagame.asteroids.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import op.javagame.asteroids.ecs.components.*;

/**
 * Система движения объектов (игрок, астероиды и др.).
 */
public class MovementSystem extends EntitySystem {
    private final Camera camera;
    private ImmutableArray<Entity> entities;

    private final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);
    private final ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);
    private final ComponentMapper<LaserComponent> laserMapper = ComponentMapper.getFor(LaserComponent.class);

    public MovementSystem(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        float worldWidth = camera.viewportWidth;
        float worldHeight = camera.viewportHeight;

        for (Entity entity : entities) {
            PositionComponent pos = positionMapper.get(entity);
            VelocityComponent vel = velocityMapper.get(entity);
            TextureComponent tex = textureMapper.get(entity);

            if (pos == null || vel == null || tex == null) continue;

            pos.position.add(vel.velocity.cpy().scl(deltaTime));

            if (laserMapper.has(entity)) {
                if (isOutOfScreen(pos.position, worldWidth, worldHeight)) {
                    getEngine().removeEntity(entity);
                }
                continue;
            }

            float objectWidth = tex.texture.getWidth() / 2f;
            float objectHeight = tex.texture.getHeight() / 2f;

            wrapAroundScreen(pos.position, worldWidth, worldHeight, objectWidth, objectHeight);
        }
    }

    private void wrapAroundScreen(Vector2 position, float screenWidth, float screenHeight, float objectWidth, float objectHeight) {
        if (position.x < -objectWidth) position.x = screenWidth + objectWidth;
        if (position.x > screenWidth + objectWidth) position.x = -objectWidth;
        if (position.y < -objectHeight) position.y = screenHeight + objectHeight;
        if (position.y > screenHeight + objectHeight) position.y = -objectHeight;
    }

    private boolean isOutOfScreen(Vector2 position, float screenWidth, float screenHeight) {
        return position.x < 0 || position.x > screenWidth || position.y < 0 || position.y > screenHeight;
    }
}
