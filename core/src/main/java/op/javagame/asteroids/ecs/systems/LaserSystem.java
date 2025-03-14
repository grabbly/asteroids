package op.javagame.asteroids.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import op.javagame.asteroids.ecs.components.PositionComponent;
import op.javagame.asteroids.ecs.components.VelocityComponent;

public class LaserSystem extends EntitySystem {
    private static final float SCREEN_MARGIN = 50f;
    private ImmutableArray<Entity> lasers;
    private final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<VelocityComponent> velocityMapper = ComponentMapper.getFor(VelocityComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        lasers = engine.getEntitiesFor(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity laser : lasers) {
            PositionComponent pos = positionMapper.get(laser);
            VelocityComponent vel = velocityMapper.get(laser);

            if (pos == null || vel == null) continue;

            pos.position.add(vel.velocity.cpy().scl(deltaTime));

            if (isOutOfScreen(pos.position)) {
                getEngine().removeEntity(laser);
                Gdx.app.log("LaserSystem", "Laser removed!");
            }
        }
    }

    private boolean isOutOfScreen(Vector2 position) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        return position.x < -SCREEN_MARGIN || position.x > screenWidth + SCREEN_MARGIN ||
            position.y < -SCREEN_MARGIN || position.y > screenHeight + SCREEN_MARGIN;
    }
}
