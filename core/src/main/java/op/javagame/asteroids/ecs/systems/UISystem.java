package op.javagame.asteroids.ecs.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UISystem extends EntitySystem {
    private Stage stage;
    private Skin skin;
    private Label scoreLabel;
    private Label fpsLabel;
    private Label screenSizeLabel;

    private final OrthographicCamera camera;
    private int score = 0;  // Тестовый счет

    public UISystem(OrthographicCamera camera) {
        this.camera = camera;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Загружаем скин (нужно добавить assets/ui/uiskin.json)
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Создаём таблицу для UI
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // FPS Label
        fpsLabel = new Label("FPS: 0", skin);
        table.top().left().pad(10);
        table.add(fpsLabel).row();

        // Screen Size Label
        screenSizeLabel = new Label("Screen: 0 x 0", skin);
        table.add(screenSizeLabel).pad(5).row();


        // Score Label
        scoreLabel = new Label("Score: " + score, skin);
        table.add(scoreLabel).pad(10);
    }

    @Override
    public void update(float deltaTime) {
        int fps = Gdx.graphics.getFramesPerSecond();
        fpsLabel.setText("FPS: " + fps);

        screenSizeLabel.setText("Screen: " + Gdx.graphics.getWidth() + " x " + Gdx.graphics.getHeight());

        stage.act(deltaTime);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);  // ✅ UI масштабируется под новый размер окна
    }

    public void increaseScore() {
        score++;
        scoreLabel.setText("Score: " + score);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
