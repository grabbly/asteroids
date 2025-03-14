package op.javagame.asteroids.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class ColliderComponent implements Component {
    public final Vector2 center = new Vector2();  // Центр коллайдера
    public float radius;  // Радиус коллайдера

    public ColliderComponent(float radius) {
        this.radius = radius;
    }
}
