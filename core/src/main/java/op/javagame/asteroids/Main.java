package op.javagame.asteroids;

import com.badlogic.gdx.Game;
import op.javagame.asteroids.events.GameEvents;
import op.javagame.asteroids.screens.FirstScreen;
import op.javagame.asteroids.screens.GameScreen;
import op.javagame.asteroids.events.EventBus;

public class Main extends Game {
    @Override
    public void create() {
        EventBus.INSTANCE.addListener(event -> {
            if (event instanceof GameEvents.StartGameEvent) {
                startGame();
            }
        });
        setScreen(new FirstScreen());
    }
    private void startGame() {
        setScreen(new GameScreen()); // Переход в игру
    }
}
