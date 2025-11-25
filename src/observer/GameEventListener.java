package observer;

/**
 * Interface que define um observador (quem escuta os eventos).
 */
public interface GameEventListener {
    void onGameEvent(GameEvent evento);
}
