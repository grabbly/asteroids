package op.javagame.asteroids.ecs.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {
    public int lives = 3; // Player starts with 3 lives

    public HealthComponent(int lives) {
        this.lives = lives;
    }
}
