package op.javagame.asteroids.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;

public class TextureComponent implements Component {
    public Texture texture;

    public TextureComponent(String texturePath) {
        this.texture = new Texture(texturePath);
    }
}
