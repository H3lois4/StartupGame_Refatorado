package app;

import observer.ConsoleEventListener;
import observer.GameEventManager;
import ui.ConsoleApp;

public class Main {

    public static void main(String[] args) {

        // Criador do gerenciador de eventos
        GameEventManager eventManager = new GameEventManager();

        // Adiciona um listener padrão do console
        eventManager.addListener(new ConsoleEventListener());

        // Inicializa a interface de console
        ConsoleApp app = new ConsoleApp(eventManager);

        // Inicia o jogo
        app.iniciar();
    }
}
