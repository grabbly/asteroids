package op.javagame.asteroids.ecs.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import op.javagame.asteroids.events.EventBus;
import op.javagame.asteroids.events.GameEvents;

public class UISystem extends EntitySystem {
    private Stage stage;
    private Skin skin;
    private Label scoreLabel;
//    private Label fpsLabel;
//    private Label screenSizeLabel;

    private Table livesTable; // New table for life sprites
    private Texture lifeTexture; // Ship sprite for each life
    private Texture lifeTextureSmall; // Ship sprite for each life

    private final OrthographicCamera camera;
    private int score = 0;
    private int lives = 3; // Default player lives

    public UISystem(OrthographicCamera camera) {
        this.camera = camera;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load UI skin (needs assets/ui/uiskin.json)
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create a main table for the existing UI elements
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // FPS Label
//        fpsLabel = new Label("FPS: 0", skin);
        mainTable.top().left().pad(10);
//        mainTable.add(fpsLabel).row();

        // Screen Size Label
//        screenSizeLabel = new Label("Screen: 0 x 0", skin);
//        mainTable.add(screenSizeLabel).pad(5).row();

        // Score Label
        scoreLabel = new Label("Score: " + score, skin);
        mainTable.add(scoreLabel).pad(10).row();


        // Create a separate table for life sprites in the top-right corner
        livesTable = new Table();
        livesTable.top().right();
        livesTable.setFillParent(true);
        stage.addActor(livesTable);

        // Load the ship texture to represent each life
        lifeTexture = new Texture(Gdx.files.internal("textures/ships/playerShip1_blue.png"));
        lifeTextureSmall = new Texture(Gdx.files.internal("textures/ships/playerShip1_blue_small.png"));

        // Draw initial life sprites
        updateLifeSprites();

        // Event Listeners
        EventBus.INSTANCE.addListener(event -> {
            if (event instanceof GameEvents.AsteroidDestroyedEvent) {
                increaseScore(10);
            }
        });
        EventBus.INSTANCE.addListener(event -> {
            if (event instanceof GameEvents.ResetScoreEvent) {
                resetScore();
            }
        });
        EventBus.INSTANCE.addListener(event -> {
            if (event instanceof GameEvents.PlayerHitEvent) {
                updateLives(((GameEvents.PlayerHitEvent) event).remainingLives);
            }
        });
        EventBus.INSTANCE.addListener(event -> {
            if (event instanceof GameEvents.RestartGameEvent) {
                resetUI();
            }
        });
    }

    @Override
    public void update(float deltaTime) {
//        int fps = Gdx.graphics.getFramesPerSecond();
//        fpsLabel.setText("FPS: " + fps);
//        screenSizeLabel.setText("Screen: " + Gdx.graphics.getWidth() + " x " + Gdx.graphics.getHeight());
        stage.act(deltaTime);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void increaseScore(int points) {
        score += points;
        scoreLabel.setText("Score: " + score);
    }

    public void resetScore() {
        score = 0;
        scoreLabel.setText("Score: " + score);
    }

    /**
     * Updates the lives value and refreshes both text label and sprite icons.
     */
    public void updateLives(int newLives) {
        lives = newLives;
        updateLifeSprites();                  // Refresh sprite icons
    }

    /**
     * Clears and re-draws the life sprites in the top-right corner.
     */
    private void updateLifeSprites() {
        livesTable.clearChildren();
        for (int i = 0; i < lives; i++) {
            Image lifeImg = new Image(new TextureRegionDrawable(new TextureRegion(lifeTextureSmall)));
            lifeImg.setSize(16, 16);
            livesTable.add(lifeImg).pad(20);
        }
    }

    public void resetUI() {
        score = 0;
        lives = 3;
        scoreLabel.setText("Score: " + score);
        updateLifeSprites();
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
        lifeTexture.dispose(); // Dispose the ship texture
        lifeTextureSmall.dispose(); // Dispose the ship texture
    }
}
