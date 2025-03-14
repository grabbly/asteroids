package op.javagame.asteroids.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager {
    private static ScreenManager instance;
    private Game game;

    private ScreenManager(Game game) {
        this.game = game;
    }

    public static void initialize(Game game) {
        if (instance == null) {
            instance = new ScreenManager(game);
        }
    }

    public static ScreenManager getInstance() {
        return instance;
    }

    public void setScreen(Screen screen) {
        game.setScreen(screen);
    }
}
