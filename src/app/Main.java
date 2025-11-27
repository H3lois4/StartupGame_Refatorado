package app;

import config.Config;
import engine.GameEngine;
import observer.ConsoleEventListener;
import ui.ConsoleApp;

public class Main {

    public static void main(String[] args) {

        try {
            Config config = new Config();

            // Registra listener no observer central do GameEngine
            GameEngine.getEventManager().addListener(new ConsoleEventListener());

            ConsoleApp app = new ConsoleApp(config);
            app.iniciar();

        } catch (Exception e) {
            System.err.println("Erro ao iniciar o jogo: " + e.getMessage());
        }
    }
}
