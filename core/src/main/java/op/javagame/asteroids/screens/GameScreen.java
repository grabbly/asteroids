package op.javagame.asteroids.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import op.javagame.asteroids.ecs.factories.GameEntityFactory;
import op.javagame.asteroids.ecs.systems.*;
import op.javagame.asteroids.events.EventBus;
import op.javagame.asteroids.events.GameEvents;

/**
 * Основной игровой экран.
 */
public class GameScreen implements Screen {
    private Engine engine;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private int screenWidth = 800;
    private int screenHeight = 600;
    private Viewport viewport;
    public GameScreen() {
        initialize();
    }

    private void initialize() {
        Gdx.app.log("GameScreen", "Initializing game");

        batch = new SpriteBatch();
        setupCamera();
        setupEngine();

        EventBus.INSTANCE.addListener(event -> {
            if (event instanceof GameEvents.RestartGameEvent) {
                restartGame();
                Gdx.app.log("GameScreen", "RestartGameEvent");
            }
        });

    }

    private void setupCamera() {
        float worldWidth = 20f;  // 📏 Увеличили ширину мира (было 10, стало 20)
        float worldHeight = worldWidth * (Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth());

        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
        camera.update();
    }

    private void setupEngine() {
        engine = new Engine();
        engine.addSystem(new PlayerSystem(new Vector2((float) screenWidth /2, (float) screenHeight /2)));
        engine.addSystem(new MovementSystem(camera));
        engine.addSystem(new RenderSystem(batch, camera));
        engine.addSystem(new AsteroidSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new LaserSystem());
        engine.addSystem(new UISystem(camera));

    }
    private void restartGame() {
        Gdx.app.log("GameScreen", "🔄 Restarting game...");

        engine.removeAllEntities();

        // ✅ Пересоздаём игровые системы
        setupEngine();
        setupBackground();
    }
    private void setupBackground(){
        engine.addEntity(GameEntityFactory.createBackground());
    }
    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.setToOrtho(false, width, height);
        camera.position.set(width / 2f, height / 2f, 0);
        camera.update();
    }

    @Override
    public void dispose() {
        Gdx.app.log("GameScreen", "Disposing resources...");
        batch.dispose();
    }

    @Override
    public void show() {
        setupBackground();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
