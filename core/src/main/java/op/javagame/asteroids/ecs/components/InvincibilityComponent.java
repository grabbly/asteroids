package op.javagame.asteroids.ecs.components;

import com.badlogic.ashley.core.Component;

public class InvincibilityComponent implements Component {
    public float invincibilityTime = 2.0f; // Time in seconds while invincible
    public boolean isBlinking = false; // Used for visual blinking effect
}
