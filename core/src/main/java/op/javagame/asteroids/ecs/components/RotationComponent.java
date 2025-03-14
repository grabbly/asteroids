package op.javagame.asteroids.ecs.components;

import com.badlogic.ashley.core.Component;

public class RotationComponent implements Component {
    public float angle = 0;

    public RotationComponent() {}

    public RotationComponent(float angle) {
        this.angle = angle;
    }
}
