package app;

import observer.ConsoleEventListener;
import observer.GameEventManager;
import ui.ConsoleApp;

public class Main {

    public static void main(String[] args) {

        // Cria o gerenciador de eventos
        GameEventManager eventManager = new GameEventManager();

        // Adiciona um observador (listener)
        eventManager.addListener(new ConsoleEventListener());

        // Inicializa a aplicação
        ConsoleApp app = new ConsoleApp(eventManager);
        app.iniciar();
    }
}
