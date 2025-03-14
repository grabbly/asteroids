package op.javagame.asteroids.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
    public Vector2 velocity = new Vector2();

    public VelocityComponent(float x, float y) {
        this.velocity.set(x, y);
    }

    public VelocityComponent(Vector2 velocity) {
        this.velocity.set(velocity);
    }
}
