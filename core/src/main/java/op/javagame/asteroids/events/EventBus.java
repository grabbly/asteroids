package op.javagame.asteroids.events;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventBus {
    public static final EventBus INSTANCE = new EventBus();
    private final CopyOnWriteArrayList<Consumer<Object>> listeners = new CopyOnWriteArrayList<>();

    private EventBus() {} // Singleton

    public void addListener(Consumer<Object> listener) {
        listeners.add(listener);
    }

    public void notify(Object event) {
        for (Consumer<Object> listener : listeners) {
            listener.accept(event);
        }
    }
}
