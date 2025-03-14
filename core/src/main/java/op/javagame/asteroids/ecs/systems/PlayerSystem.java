package op.javagame.asteroids.ecs.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import op.javagame.asteroids.ecs.components.PositionComponent;
import op.javagame.asteroids.ecs.components.RotationComponent;
import op.javagame.asteroids.ecs.factories.GameEntityFactory;

public class PlayerSystem extends EntitySystem {
    private static final float FORWARD_ACCELERATION = 800f;
    private static final float STRAFE_ACCELERATION = 600f;
    private static final float FRICTION = 0.99f;
    private static final float MAX_SPEED = 600f;
    private static final float MAX_STRAFE_SPEED = 400f;
    private static final float FIRE_COOLDOWN = 0.3f;

    private Entity player;
    private Vector2 velocity = new Vector2();
    private Vector2 strafeVelocity = new Vector2();
    private float fireCooldownTimer = 0;

    public PlayerSystem(Vector2 startPos) {
        this.player = GameEntityFactory.createPlayer(startPos);
    }

    @Override
    public void addedToEngine(Engine engine) {
        engine.addEntity(player);
    }

    @Override
    public void update(float deltaTime) {
        if (player == null) return;

        PositionComponent position = player.getComponent(PositionComponent.class);
        RotationComponent rotation = player.getComponent(RotationComponent.class);
        if (position == null || rotation == null) return;

        fireCooldownTimer -= deltaTime;

        Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        Vector2 playerPosition = position.position;
        Vector2 direction = new Vector2(mousePosition).sub(playerPosition);
        rotation.angle = direction.angleDeg()-90f;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            Vector2 thrust = getForwardVector(rotation.angle+90).scl(FORWARD_ACCELERATION * deltaTime);
            velocity.add(thrust);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            Vector2 strafeLeft = getStrafeVector(rotation.angle+90, STRAFE_ACCELERATION * deltaTime);
            strafeVelocity.add(strafeLeft);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            Vector2 strafeRight = getStrafeVector(rotation.angle+90, -STRAFE_ACCELERATION * deltaTime);
            strafeVelocity.add(strafeRight);
        }

        if ((Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && fireCooldownTimer <= 0) {
            shootLaser(position.position, rotation.angle);
            fireCooldownTimer = FIRE_COOLDOWN;
        }

        velocity.scl(FRICTION);
        strafeVelocity.scl(FRICTION);

        if (velocity.len() > MAX_SPEED) velocity.setLength(MAX_SPEED);
        if (strafeVelocity.len() > MAX_STRAFE_SPEED) strafeVelocity.setLength(MAX_STRAFE_SPEED);

        position.position.add(velocity.cpy().scl(deltaTime));
        position.position.add(strafeVelocity.cpy().scl(deltaTime));
    }

    private void shootLaser(Vector2 startPos, float angle) {
        Engine engine = getEngine();
        if (engine == null) return;

        Entity laser = GameEntityFactory.createLaser(startPos, angle);
        engine.addEntity(laser);
        Gdx.app.log("PlayerSystem", "Laser fired!");
    }


    private Vector2 getForwardVector(float angle) {
        float rad = (float) Math.toRadians(angle);
        return new Vector2((float) Math.cos(rad), (float) Math.sin(rad));
    }

    private Vector2 getStrafeVector(float angle, float speed) {
        float rad = (float) Math.toRadians(angle);
        return new Vector2((float) -Math.sin(rad) * speed, (float) Math.cos(rad) * speed);
    }
}
