package ui;

import engine.GameEngine;
import model.Startup;
import config.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    private final Scanner in = new Scanner(System.in);
    private final GameEngine engine;

    public ConsoleApp(Config config) {
        // cria o motor do jogo com config (usa o observer central em GameEngine)
        this.engine = new GameEngine(config);
    }

    public void iniciar() {
        System.out.println("=== Startup Game (Console) ===");

        List<Startup> startups = criarStartupsIniciais();

        engine.executarJogo(startups, in);
    }

    private List<Startup> criarStartupsIniciais() {
        List<Startup> list = new ArrayList<>();

        list.add(new Startup("AlphaTech", 100000, 10000, 50, 60));
        list.add(new Startup("BioNova", 80000, 7000, 55, 55));
        list.add(new Startup("CloudZ", 110000, 12000, 45, 65));

        return list;
    }
}
