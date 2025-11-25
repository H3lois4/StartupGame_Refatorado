package observer;


import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia os observadores e envia notificações.
 */
public class GameEventManager {

    private final List<GameEventListener> listeners = new ArrayList<>();

    // Adiciona um novo observador
    public void addListener(GameEventListener listener) {
        listeners.add(listener);
    }

    // Remove um observador
    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    // Notifica todos os observadores sobre um evento
    public void notify(GameEvent evento) {
        for (GameEventListener listener : listeners) {
            listener.onGameEvent(evento);
        }
    }
}
