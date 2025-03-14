package op.javagame.asteroids.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import op.javagame.asteroids.ecs.components.BackgroundComponent;
import op.javagame.asteroids.ecs.components.PositionComponent;
import op.javagame.asteroids.ecs.components.RotationComponent;
import op.javagame.asteroids.ecs.components.TextureComponent;

/**
 * Система отрисовки всех игровых объектов.
 */
public class RenderSystem extends EntitySystem {
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private ImmutableArray<Entity> entities;
    private final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);
    private final ComponentMapper<RotationComponent> rotationMapper = ComponentMapper.getFor(RotationComponent.class);
    private ImmutableArray<Entity> backgroundEntities;
    private final ComponentMapper<BackgroundComponent> backgroundMapper = ComponentMapper.getFor(BackgroundComponent.class);

    public RenderSystem(SpriteBatch batch, OrthographicCamera camera) {
        this.batch = batch;
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PositionComponent.class, TextureComponent.class).get());
        backgroundEntities = engine.getEntitiesFor(Family.all(BackgroundComponent.class).get());
    }
    private void drawBackground() {
        for (Entity entity : backgroundEntities) {
            BackgroundComponent bg = backgroundMapper.get(entity);
            if (bg == null || bg.tileTexture == null) continue;

            float tileWidth = bg.tileTexture.getRegionWidth();
            float tileHeight = bg.tileTexture.getRegionHeight();
            float screenWidth = Gdx.graphics.getWidth();
            float screenHeight = Gdx.graphics.getHeight();

            for (float x = 0; x < screenWidth; x += tileWidth) {
                for (float y = 0; y < screenHeight; y += tileHeight) {
                    batch.draw(bg.tileTexture, x, y);
                }
            }
        }
    }
    @Override
    public void update(float deltaTime) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawBackground();

        for (Entity entity : entities) {
            PositionComponent pos = positionMapper.get(entity);
            TextureComponent tex = textureMapper.get(entity);
            RotationComponent rot = rotationMapper.get(entity);

            if (pos != null && tex != null && tex.visible) {
                float angle = (rot != null) ? rot.angle : 0;
                TextureRegion region = new TextureRegion(tex.texture);

                float scaleFactor = 1 / 1.5f;
                float width = region.getRegionWidth() * scaleFactor;
                float height = region.getRegionHeight() * scaleFactor;

                batch.draw(region,
                    pos.position.x - width / 2f,
                    pos.position.y - height / 2f,  // Центрирование
                    width / 2f, height / 2f,  // Точка вращения
                    width, height,  // Размер
                    1, 1, angle);
            }
        }

        batch.end();
    }
}
