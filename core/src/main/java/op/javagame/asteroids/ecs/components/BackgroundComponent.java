package op.javagame.asteroids.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BackgroundComponent implements Component {
    public TextureRegion tileTexture;

    public BackgroundComponent(String texturePath) {
        Texture texture = new Texture(texturePath);
        this.tileTexture = new TextureRegion(texture);
    }
}
