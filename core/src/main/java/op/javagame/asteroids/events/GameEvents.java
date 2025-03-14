package op.javagame.asteroids.events;

/**
 * Определяет события игры.
 */
public class GameEvents {
    public static class StartGameEvent {}
    public static class RestartGameEvent {}
    public static class ResetScoreEvent {}
    public static class AsteroidDestroyedEvent {}
    public static class PlayerHitEvent {
        public final int remainingLives;
        public PlayerHitEvent(int lives) {
            this.remainingLives = lives;
        }
    }
    public static class CameraShakeEvent {
        public final float duration;
        public final float intensity;

        public CameraShakeEvent(float duration, float intensity) {
            this.duration = duration;
            this.intensity = intensity;
        }
    }
}
