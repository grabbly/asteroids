package op.javagame.asteroids.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import op.javagame.asteroids.ecs.factories.GameEntityFactory;
import op.javagame.asteroids.ecs.systems.*;
import op.javagame.asteroids.events.EventBus;
import op.javagame.asteroids.events.GameEvents;

public class GameScreen implements Screen {
    private Engine engine;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private int screenWidth = 800;
    private int screenHeight = 600;
    private Viewport viewport;

    private float shakeDuration = 0;
    private float shakeIntensity = 0.5f;

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

        EventBus.INSTANCE.addListener(event -> {
            if (event instanceof GameEvents.CameraShakeEvent) {
                GameEvents.CameraShakeEvent shakeEvent = (GameEvents.CameraShakeEvent) event;
                shakeCamera(shakeEvent.duration, shakeEvent.intensity);
            }
        });
    }

    private void setupCamera() {
        float worldWidth = 20f;
        float worldHeight = worldWidth * (Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth());

        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
        normalCameraPosition = new Vector2(worldWidth / 2f, worldHeight / 2f);
        camera.update();
    }

    private void setupEngine() {
        engine = new Engine();
        engine.addSystem(new PlayerSystem(new Vector2((float) screenWidth / 2, (float) screenHeight / 2)));
        engine.addSystem(new MovementSystem(camera));
        engine.addSystem(new RenderSystem(batch, camera));
        engine.addSystem(new AsteroidSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new LaserSystem());
        engine.addSystem(new UISystem(camera));
    }

    private void restartGame() {
        Gdx.app.log("GameScreen", "Restarting game...");

        engine.removeAllEntities();
        EventBus.INSTANCE.notify(new GameEvents.ResetScoreEvent());
        setupEngine();
        setupBackground();
    }

    private void setupBackground() {
        engine.addEntity(GameEntityFactory.createBackground());
    }

    public void shakeCamera(float duration, float intensity) {
        shakeDuration = duration;
        shakeIntensity = intensity;
    }
    private Vector2 normalCameraPosition;
    @Override
    public void render(float delta) {

        if (shakeDuration > 0) {

            shakeDuration -= delta;

            float shakeX = MathUtils.random(-shakeIntensity, shakeIntensity);
            float shakeY = MathUtils.random(-shakeIntensity, shakeIntensity);
            camera.position.add(shakeX, shakeY, 0);
        }else{
            camera.position.set(normalCameraPosition.x, normalCameraPosition.y, 0);
        }

        camera.update();
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.setToOrtho(false, width, height);
        normalCameraPosition = new Vector2(width / 2f, height / 2f);
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
