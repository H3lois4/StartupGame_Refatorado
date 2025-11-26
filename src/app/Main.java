package app;

import config.Config;
import observer.ConsoleEventListener;
import observer.GameEventManager;
import ui.ConsoleApp;

public class Main {

    public static void main(String[] args) {

        try {
            Config config = new Config();

            GameEventManager eventManager = new GameEventManager();
            eventManager.addListener(new ConsoleEventListener());

            ConsoleApp app = new ConsoleApp(eventManager, config);
            app.iniciar();

        } catch (Exception e) {
            System.err.println("Erro ao iniciar o jogo: " + e.getMessage());
        }
    }
}
