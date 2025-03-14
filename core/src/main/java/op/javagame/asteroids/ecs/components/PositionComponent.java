package op.javagame.asteroids.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {
    public Vector2 position;

    public PositionComponent() {
        this.position = new Vector2(); // По умолчанию (0, 0)
    }

    public PositionComponent(float x, float y) {
        this.position = new Vector2(x, y);
    }

    public PositionComponent(Vector2 position) {
        this.position = position;
    }
}
