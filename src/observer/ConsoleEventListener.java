package observer;

public class ConsoleEventListener implements GameEventListener {

    @Override
    public void onGameEvent(GameEvent evento) {
        System.out.println("[EVENTO] " + evento.getTipo() + ": " + evento.getDescricao());
    }
}
