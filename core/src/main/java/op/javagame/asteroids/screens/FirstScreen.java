package op.javagame.asteroids.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import op.javagame.asteroids.events.EventBus;
import op.javagame.asteroids.events.GameEvents;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    private Texture logoTexture;
    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    private Stage stage;
    private Skin skin;
    private TextButton startButton;

    public FirstScreen() {
        Gdx.app.log("MyTag", "FirstScreen Initialized");

        // Загружаем логотип
        logoTexture = new Texture("libgdx.png");

        // Создаём 2D рендеринг
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(800, 600);

        // UI: создаём сцену
        stage = new Stage(new FitViewport(800, 600));
        Gdx.input.setInputProcessor(stage); // Обрабатываем ввод для UI

        // Загружаем UI-скин
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Создаём кнопку "Start Game"
        startButton = new TextButton("Start Game", skin);
        startButton.setSize(200, 50);
        startButton.setPosition(300, 150); // Позиция кнопки

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("UI", "Start Button Clicked!");
                EventBus.INSTANCE.notify(new GameEvents.StartGameEvent());
            }
        });
        stage.addActor(startButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(logoTexture, 300, 300, 200, 100);
        spriteBatch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        logoTexture.dispose();
        stage.dispose();
        skin.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
